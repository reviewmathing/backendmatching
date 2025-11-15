package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
public class ReviewAssigmentDto {

    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    public static class ListView {
        private Page<ListUnit> items;

        public ListView(Page<ReviewAssignment> reviewAssignments, List<Mission> missions) {
            items = reviewAssignments.map(ra -> toUnit(missions, ra));
        }

        private ListUnit toUnit(List<Mission> missions, ReviewAssignment reviewAssignment) {
            String name = missions.stream().filter(m -> m.getId().equals(reviewAssignment.getMissionId()))
                    .map(Mission::getTitle).findAny().orElse(
                            "Unknown");
            return new ListUnit(
                    name,
                    reviewAssignment.getReviewAssignmentStatus(),
                    reviewAssignment.getId()
            );
        }
    }

    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    public static class ListUnit {

        private String missionName;
        private ReviewAssignmentStatus status;
        private Long reviewAssigmentId;

        public ListUnit(String missionName, ReviewAssignmentStatus status, Long reviewAssigmentId) {
            this.missionName = missionName;
            this.status = status;
            this.reviewAssigmentId = reviewAssigmentId;
        }
    }
}
