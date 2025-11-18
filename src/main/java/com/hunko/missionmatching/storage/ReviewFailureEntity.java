package com.hunko.missionmatching.storage;

import com.hunko.missionmatching.storage.ReviewFailureEntity.ReviewFailureKey;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(ReviewFailureKey.class)
public class ReviewFailureEntity {

    @Id
    private Long missionId;

    @Id
    private Long reviewerId;

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewFailureKey implements Serializable {
        private Long missionId;
        private Long reviewerId;


    }
}
