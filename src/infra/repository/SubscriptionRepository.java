package infra.repository;

import domain.Subscription;
import domain.common.Money;
import infra.util.FileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionRepository {
    private static final List<Subscription> STORE;

    static {
        List<Subscription> loaded = FileStore.load("subscriptions.dat");
        if (loaded != null) {
            STORE = loaded;
        } else {
            STORE = new ArrayList<>();
            initDefaults();
        }
    }

    private static void initDefaults() {
        Subscription s = new Subscription();
        s.setSubscriptionNo("20260401-0001");
        s.setApplicantName("박수현");
        s.setSsn("020101-3******");
        s.setAddress("서울시 강남구");
        s.setCarNumber("64마0866");
        s.setChassisNumber("KMHCT41DBLU123");
        s.setProductName("MZ세대 다이렉트 차보험");
        s.setPremium(new Money(2_907_200L, "KRW"));
        s.setBasePremium(new Money(2_794_010L, "KRW"));
        s.setSubscriptionDate("2026-04-01");
        s.setStatus(Subscription.Status.PENDING_REVIEW);
        s.setOccupation("대학생");
        s.setAge(24);
        s.setCoveragesDescription("대인I/II, 대물 5억, 자상 1억, 무보험 2억, 자차 가입");
        STORE.add(s);

        FileStore.save("subscriptions.dat", STORE);
    }

    public static List<Subscription> findAll() {
        return new ArrayList<>(STORE);
    }

    public static List<Subscription> findPendingReview() {
        return STORE.stream()
            .filter(s -> s.getStatus() == Subscription.Status.PENDING_REVIEW)
            .collect(Collectors.toList());
    }

    public static Subscription findByNo(String subscriptionNo) {
        return STORE.stream()
            .filter(s -> s.getSubscriptionNo().equals(subscriptionNo))
            .findFirst().orElse(null);
    }

    public static void updateStatus(String subscriptionNo, Subscription.Status status) {
        STORE.stream()
            .filter(s -> s.getSubscriptionNo().equals(subscriptionNo))
            .findFirst()
            .ifPresent(s -> s.setStatus(status));
        FileStore.save("subscriptions.dat", STORE);
    }
}
