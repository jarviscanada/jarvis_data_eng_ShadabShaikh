package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//@Api(value = "quote", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Controller
@RequestMapping("/quote")
public class QuoteController {

  private QuoteService quoteService;

  @Autowired
  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  //@ApiOperation(value = "Show iexQuote", notes = "Show iexQuote for a given ticker/symbol")
  //@ApiOperation(value = {@ApiResponse(code = 404, message = "ticker is not found)"})
  @GetMapping(path = "/iex/ticker/{ticker}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public IexQuote getQuote(@PathVariable String ticker) {
    try {
      return quoteService.findIexQuoteByTicker(ticker.toUpperCase());
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  @PutMapping(path = "/iexMarketData")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Quote> updateMarketData() {
    try {
      return quoteService.updateMarketData();
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  //@ApiOperation(value = "Update a given quote in the quote table",
  //notes = "Manually update a quote in the quote table using IEX market data")
  @PutMapping(path = "/")
  @ResponseStatus
  @ResponseBody
  public Quote putQuote(@RequestBody Quote quote) {
    try {
      return quoteService.saveQuote(quote);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  //@ApiOperation(value = "Add a new ticker to the dailyList (quote table)",
  // notes = "Add a new ticker/symbol to the quote table, so the trade can trade this security.")
  @PostMapping(path = "/tickerId/{tickerId}")
  @ResponseStatus(HttpStatus.CREATED)
  //@ApiResponses(value={@ApiResponse(code = 404, message = "ticker not found in the IEX system")})
  @ResponseBody
  public Quote createQuote(@PathVariable String tickerId) {
    try {
      return quoteService.saveQuote(tickerId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }


  //@ApiOperation(value = "Show the dailyList", notes = "Show the dailyList for this trading system.")
  @GetMapping(path = "/dailyList")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public List<Quote> getDailyList() {
    try {
      return quoteService.findAllQuotes();
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}