package fi.paivola.mapserver.core;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author juhani
 */
public class ModelRunner implements Runnable {

    private Model m;
    private DataFrame last, current;
    private CountDownLatch latch;
    private boolean isFirst;

    public ModelRunner(CountDownLatch latch, Model m, DataFrame last, DataFrame current, boolean isFirst) {
        this.m = m;
        this.last = last;
        this.current = current;
        this.isFirst = isFirst;
        this.latch = latch;
    }

    @Override
    public void run() {
        if (this.isFirst) {
            m.onActualGenerateDefaults(current);
        } else {
            m.onTickStart(last, current);
        }
        latch.countDown();
    }

}
