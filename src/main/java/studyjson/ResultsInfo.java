package studyjson;

import model.ResultsTableModel;

import java.util.List;
import java.util.Objects;

public class ResultsInfo {

    String algorithmName;
    String code;
    List<ResultsTableModel.Row> rows;

    public ResultsInfo(String algorithmName, String code, List<ResultsTableModel.Row> rows) {
        this.algorithmName = algorithmName;
        this.code = code;
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultsInfo info = (ResultsInfo) o;
        return Objects.equals(algorithmName, info.algorithmName) &&
                Objects.equals(code, info.code) && Objects.equals(rows, info.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmName, code, rows);
    }
}
