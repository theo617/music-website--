package com.example.liu.model.request;

import com.example.liu.model.domain.Appeals;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 544
 * @Description:
 * @date 2024/5/30 11:47
 */
@Data
public class AppealsRequest {
    private int id;

    private int complaintId;

    private int userId;

    private String reason;

    private Appeals.Status status;

    private Timestamp createAt;

    private Timestamp updateAt;
}
