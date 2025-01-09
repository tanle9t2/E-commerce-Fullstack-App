package com.tanle.e_commerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "options")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer id;
    @Column(name = "option_name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "option_id")
    private List<OptionValue> optionValues;

    public Option(String name, List<OptionValue> optionValues) {
        this(0,name,optionValues);
    }

    public List<OptionValue> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<OptionValue> optionValues) {
        this.optionValues = optionValues;
    }

    public Integer parseIndexOptionValue(OptionValue optionValue) {
        return optionValues.indexOf(optionValue);
    }

}
