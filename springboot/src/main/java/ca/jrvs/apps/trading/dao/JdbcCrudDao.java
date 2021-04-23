package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

  abstract public JdbcTemplate getJdbcTemplate();

  abstract public SimpleJdbcInsert getSimpleJdbcInsert();

  abstract public String getTableName();

  abstract public String getIdColumnName();

  abstract Class<T> getEntityClass();

  @Override
  public <S extends T> S save(S entity) {
    if (existsById(entity.getId())) {
      if (updateOne(entity) != 1) {
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    } else {
      addOne(entity);
    }
    return entity;
  }

  /**
   * Helper method that saves one quote
   * @param entity
   * @param <S>
   */
  private <S extends T> void addOne(S entity){
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);

    Number newId = getSimpleJdbcInsert().executeAndReturnKey(parameterSource);
    entity.setId(newId.intValue());
  }

  abstract public int updateOne(T entity);

  @Override
  public Optional<T> findById(Integer id) {
    Optional<T> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";

    try {
      entity = Optional.ofNullable((T) getJdbcTemplate().queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(getEntityClass()), id));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find trader id:" + id, e);
    }
    return entity;
  }

  @Override
  public boolean existsById(Integer id) {
    String existSql = "SELECT COUNT(*) FROM " + getTableName()
        + " WHERE ticker =?";
    int count = getJdbcTemplate().queryForObject(existSql, Integer.class, id);
    return count > 0;
  }

  @Override
  public List<T> findAllById(Iterable<Integer> ids){
    List<T> foundQuotes = new ArrayList<>();
        ids.forEach(t -> {
          foundQuotes.add(findById(t).get());
        });
        return foundQuotes;
  }

  @Override
  public void deleteById(Integer id) {
    if (!existsById(id)) {
      throw new IllegalArgumentException("ID was not found.");
    }
    String deleteSql = "DELETE FROM " + getTableName() + " WHERE " + id + " =?";
    getJdbcTemplate().update(deleteSql, id);
  }

  @Override
  public long count() {
    String countString = "SELECT COUNT(*) FROM " + getTableName();
    Long count = getJdbcTemplate().queryForObject(countString, Long.class);
    if (count == null)
      throw new NullPointerException("SQL Count Null returned");
    return count;
  }

  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + getTableName();
    getJdbcTemplate().update(deleteSql);
  }

}
