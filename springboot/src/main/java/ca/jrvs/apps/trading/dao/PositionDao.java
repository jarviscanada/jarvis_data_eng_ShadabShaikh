package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Pos;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao {

  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);

  private static final String TABLE_NAME = "position";
  private static final String ID_COLUMN = "account_id";

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public PositionDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }

  public JdbcTemplate getJdbcTemplate() {
    return this.jdbcTemplate;
  }

  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return simpleJdbcInsert;
  }

  public String getTableName() {
    return TABLE_NAME;
  }

  public String getIdColumnName() {
    return ID_COLUMN;
  }

  Class<Position> getEntityClass() {
    return Position.class;
  }

  public boolean existsById(Integer id) {
    String existSql = "SELECT COUNT(*) FROM " + getTableName()
        + " WHERE " + getIdColumnName() + " =?";
    int count = getJdbcTemplate().queryForObject(existSql, Integer.class, id);
    return count > 0;
  }

  public Optional<Position> findById(Integer id) {
    Optional<Position> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";

    try {
      entity = Optional.ofNullable(getJdbcTemplate().queryForObject(selectSql,
          BeanPropertyRowMapper.newInstance(getEntityClass()), id));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find trader id:" + id, e);
    }
    return entity;
  }

  public List<Position> findAllById(Iterable<Integer> ids) {
    List<Position> foundEntities = new ArrayList<>();
    ids.forEach(t -> {
      foundEntities.add(findById(t).get());
    });
    return foundEntities;
  }

  public Iterable<Position> findAll() {
    String selectSql = "SELECT * FROM " + getTableName();
    List<Position> entities = getJdbcTemplate()
        .query(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()));
    return entities;
  }
  public long count() {
    String countString = "SELECT COUNT(*) FROM " + getTableName();
    Long count = getJdbcTemplate().queryForObject(countString, Long.class);
    if (count == null) {
      throw new NullPointerException("SQL Count Null returned");
    }
    return count;
  }


}
