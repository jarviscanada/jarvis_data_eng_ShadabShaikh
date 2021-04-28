package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraderAccountService {

  private static final Logger logger = LoggerFactory.getLogger(TraderAccountService.class);

  private final TraderDao traderDao;
  private final AccountDao accountDao;
  private final PositionDao positionDao;
  private final SecurityOrderDao securityOrderDao;

  @Autowired
  public TraderAccountService(TraderDao traderDao, AccountDao accountDao,
      PositionDao positionDao, SecurityOrderDao securityOrderDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.positionDao = positionDao;
    this.securityOrderDao = securityOrderDao;
  }

  private boolean validate(Trader trader) {
    try {
      Class clazz = Trader.class;
      for (Field field : clazz.getDeclaredFields())
      {
        field.setAccessible(true);
        if (field.get(trader) == null || field.get(trader).equals(""))
        {
          return false;
        }
      }
    } catch (IllegalAccessException e) {
      logger.error("Access to trader class fields not possible.", e);
    }
    return true;
  }

  private boolean validate(Integer traderId){
    return traderId != null && traderDao.existsById(traderId);
  }

  private boolean validate(Integer traderId, Double fund){
    return traderId != null && traderDao.existsById(traderId) && fund > 0;
  }

  private TraderAccountView  makeTraderAccountView(Trader trader, Account account) {
    return new TraderAccountView(trader.getId(), trader.getFirst_name(), trader.getLast_name(),
        trader.getDob(), trader.getCountry(), trader.getEmail(),
        account.getId(), account.getAmount());
  }

  /**
   * Create a new trader and initialize a new account with 0 amount.
   * - validate user input (all fields must be non empty)
   * - create a trader
   * - create an account
   * - create, setup and return a new traderAccountView
   * <p>
   * Assumption: to simplify the logic, each trader has only one account where traderId ==
   * accountId
   *
   * @param trader cannot be null. All fields cannot be null except for id (auto-generated)
   * @return traderAccountView
   * @throws IllegalArgumentException if a trader has null fields or id is not null
   */

  public TraderAccountView createTraderAndAccount(Trader trader) {
    Account account = new Account();
    if(validate(trader)) {
      traderDao.save(trader);
      account.setAmount(0d);
      account.setTrader_id(trader.getId());
      accountDao.save(account);
    } else throw new IllegalArgumentException("Error: trader account could not be validated");
    return makeTraderAccountView(trader, account);
  }

  /**
   * A trader can be deleted iff it has no open position and 0 cash balance
   * - validate traderID
   * - get trader account by traderId and check account balance
   * - get positions by accountId and check positions
   * - delete all securityOrders, account, trader (in this order)
   *
   * @param traderId must not be null
   * @throws IllegalArgumentException if tradeId is null or not found or unable to delete
   */

  public void deleteTraderById(Integer traderId) {
    if(validate(traderId)) {
      Account account = accountDao.findById(traderId).get();
      if(account.getAmount() == 0d && !positionDao.existsById(account.getId())) {
        if(securityOrderDao.existsById(account.getId())) {
          securityOrderDao.deleteById(account.getId());
        }
        accountDao.deleteById(account.getId());
        traderDao.deleteById(account.getId());
      } else throw new IllegalArgumentException("Account amount not 0 or account has open positions.");
    } else throw new IllegalArgumentException("traderId is null or doesn't exist in the table.");
  }

  /**
   * Deposit a fund to an account by traderId
   * - validate user input
   * - account = accountDao.findByTraderId
   * - accountDao.updateAmountById
   *
   * @param traderId traderId
   * @param fund     must be greater than 0
   * @return updated Account
   * @throws IllegalArgumentException if traderId is null or not found, and fund is less or equal to
   *                                  0
   */
  public Account deposit(Integer traderId, Double fund) {
    if(!validate(traderId, fund)) {
      throw new IllegalArgumentException("TraderId is null or doesn't exist or funds less than 0.");
    }
      Account account = accountDao.findById(traderId).get();
      Double newAmount = account.getAmount() + fund;
      account.setAmount(newAmount);
      accountDao.updateOne(account);
      return account;
  }

  /**
   * Withdraw a fund to an account by traderId
   *
   * - validate user input
   * - account = accountDao.findByTraderId
   * - accountDao.updateAmountById
   *
   * @param traderId
   * @param fund amount can't be 0
   * @return updated Account
   * @throws IllegalArgumentException if traderId is null or not found, fund is less or equal
   * to 0, and insufficient fund
   *
   */
  public Account withdraw(Integer traderId, Double fund) {
    if(!validate(traderId, fund)) {
      throw new IllegalArgumentException("TraderId is null or doesn't exist or funds less than 0.");
    }

    if(accountDao.findById(traderId).get().getAmount() < fund){
      throw new IllegalArgumentException("Not enough funds to withdraw.");
    }

    Account account = accountDao.findById(traderId).get();
    Double newAmount = account.getAmount() - fund;
    account.setAmount(newAmount);
    accountDao.updateOne(account);
    return account;
  }


}
