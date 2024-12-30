package com.tanle.e_commerce.utils;

import lombok.Data;

@Data
public class KafkaOperator {
    public final static String READ = "r";
    public final static String CREATE = "c";
    public final static String UPDATE = "u";
    public final static String DELETE = "d";
}
