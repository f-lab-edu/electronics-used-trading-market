package kr.flab.tradingmarket.utils;

/**
 * 스레드에서 발생한 Exception을 필드 값으로 저장해뒀다가 Test시에 저장해둔 Exception을 던져서 검증할수있도록 설계
 */
public class ThreadExceptionTester {
    private final Thread thread;
    private RuntimeException innerException;

    public ThreadExceptionTester(final Runnable runnable) {
        thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                innerException = e;
            }
        });
    }

    public void start() {
        thread.start();
    }

    public void test() throws InterruptedException {
        thread.join();
        if (innerException != null) {
            throw innerException;
        }
    }
}