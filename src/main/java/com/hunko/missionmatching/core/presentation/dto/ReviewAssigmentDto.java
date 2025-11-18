package com.hunko.missionmatching.core.presentation.dto;

import com.hunko.missionmatching.core.domain.Mission;
import com.hunko.missionmatching.core.domain.ReviewAssignment;
import com.hunko.missionmatching.core.domain.ReviewAssignmentStatus;
import com.hunko.missionmatching.core.domain.ReviewStatus;
import com.hunko.missionmatching.core.domain.Reviewee;
import com.hunko.missionmatching.core.domain.ReviewerId;
import com.hunko.missionmatching.core.domain.User;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    @EqualsAndHashCode(callSuper = false)
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Details {
        private Long missionId;
        private String missionName;
        private Long reviewAssigmentId;
        private ReviewAssignmentStatus status;
        private ZonedDateTime limitTime;
        private List<ReviewerDetails> reviewerDetails;
        private List<RevieweeDetails> revieweeDetails;

        public Details(Long missionId,String missionName, Long reviewAssigmentId, ReviewAssignmentStatus status,
                       ZonedDateTime limitTime,
                       List<ReviewerDetails> reviewerDetails,
                       List<RevieweeDetails> revieweeDetails) {
            this.missionId = missionId;
            this.missionName = missionName;
            this.reviewAssigmentId = reviewAssigmentId;
            this.status = status;
            this.limitTime = limitTime;
            this.reviewerDetails = reviewerDetails;
            this.revieweeDetails = revieweeDetails;
        }

        public static Details of(ReviewAssignment reviewAssignment, List<ReviewAssignment> reviewer, String missionName,
                                 List<User> users) {
            List<RevieweeDetails> details = toRevieweeDetails(reviewAssignment.getReviewees(), users);
            List<ReviewerDetails> reviewerDetails = toReviewerDetails(reviewAssignment.getReviewerId(), reviewer,
                    users);
            return new Details(
                    reviewAssignment.getMissionId().toLong(),
                    missionName,
                    reviewAssignment.getId(),
                    reviewAssignment.getReviewAssignmentStatus(),
                    reviewAssignment.getLimitTime(),
                    reviewerDetails,
                    details
            );
        }

        private static List<RevieweeDetails> toRevieweeDetails(List<Reviewee> revieweeList, List<User> users) {
            return revieweeList.stream().map(r -> {
                String name = users.stream().filter(u -> u.getId().equals(r.getRevieweeId().toLong())).map(
                        User::getName
                ).findAny().orElseGet(() -> "Unknown");
                return new RevieweeDetails(
                        r.getId(),
                        name,
                        r.getGithubUri().toUriString(),
                        r.getReviewStatus()
                );
            }).toList();
        }

        private static List<ReviewerDetails> toReviewerDetails(ReviewerId myId, List<ReviewAssignment> reviewerIds, List<User> users) {
            return reviewerIds.stream().map(r -> {
                ReviewerId reviewerId = r.getReviewerId();
                String name = users.stream().filter(u -> u.getId().equals(reviewerId.toLong())).map(
                        User::getName
                ).findAny().orElseGet(() -> "Unknown");
                ReviewStatus reviewStatus = r.getReviewees().stream()
                        .filter(rw -> rw.getRevieweeId().toLong().equals(myId.toLong())).map(Reviewee::getReviewStatus)
                        .findAny().orElse(null);
                return new ReviewerDetails(
                        reviewerId.toLong(),
                        name,
                        reviewStatus
                );
            }).toList();
        }
    }

    @EqualsAndHashCode
    @Getter
    @NoArgsConstructor
    @ToString
    public static class RevieweeDetails {
        private Long revieweeAssigmentId;
        private String userName;
        private String githubUri;
        private ReviewStatus status;

        public RevieweeDetails(Long revieweeAssigmentId, String userName, String githubUri, ReviewStatus status) {
            this.revieweeAssigmentId = revieweeAssigmentId;
            this.userName = userName;
            this.githubUri = githubUri;
            this.status = status;
        }
    }

    @EqualsAndHashCode
    @Getter
    @NoArgsConstructor
    @ToString
    public static class ReviewerDetails {
        private Long reviewerId;
        private String userName;
        private ReviewStatus status;

        public ReviewerDetails(Long reviewerId, String userName, ReviewStatus status) {
            this.reviewerId = reviewerId;
            this.userName = userName;
            this.status = status;
        }
    }
}
