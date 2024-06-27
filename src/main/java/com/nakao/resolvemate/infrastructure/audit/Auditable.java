package com.nakao.resolvemate.infrastructure.audit;

import com.nakao.resolvemate.infrastructure.persistance.entity.UserEntity;

import java.util.Date;

public interface Auditable {

    void setCreatedDate(Date createdDate);

    void setCreatedBy(UserEntity createdBy);

}
