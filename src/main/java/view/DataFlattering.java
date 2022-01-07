package view;

import java.util.List;

public interface DataFlattering<E> {

    Object[][] flatten (List<E> ... args);

}
