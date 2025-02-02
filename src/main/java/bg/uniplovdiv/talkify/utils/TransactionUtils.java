package bg.uniplovdiv.talkify.utils;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

import lombok.NoArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;

@NoArgsConstructor(access = PRIVATE)
public class TransactionUtils {

  public static void afterCommit(Runnable r) {
    registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            r.run();
          }
        });
  }
}
