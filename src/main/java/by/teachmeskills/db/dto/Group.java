package by.teachmeskills.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Group {
    private int id;
    private String name;
    private String teacher;
}
