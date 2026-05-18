package infra.persistence;

import java.util.List;

public interface SqlExecutor {
    int executeInsert(String sql, Object... params);
    void execute(String sql, Object... params);
    <T> T queryForObject(String sql, ResultSetExtractor<T> extractor, Object... params);
    <T> List<T> queryForList(String sql, ResultSetExtractor<T> extractor, Object... params);
}
