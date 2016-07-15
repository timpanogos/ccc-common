/*
 **  Copyright (c) 2016, Cascade Computer Consulting.
 **
 **  Permission to use, copy, modify, and/or distribute this software for any
 **  purpose with or without fee is hereby granted, provided that the above
 **  copyright notice and this permission notice appear in all copies.
 **
 **  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 **  WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 **  MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 **  ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 **  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 **  ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 **  OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.ccc.scratch;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccc.tools.ElapsedTimer;
import com.ccc.tools.RequestThrottle;

@SuppressWarnings("javadoc")
public class ThrottleTest extends Thread
{
    private final Logger log;

    public ThrottleTest()
    {
        log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void run()
    {
        RequestThrottle throttle = new RequestThrottle(10);
        ElapsedTimer.resetAllElapsedTimers();
        ElapsedTimer.resetTimer(0);
        ElapsedTimer.setLabel(0, "loop time: ");
        Producer producer = new Producer();
        int count = 0;
        do
        {
            if(++count%100 == 0)
                ElapsedTimer.resetElapsedTimers(0, 1);
            throttle.waitAsNeeded();
            producer.hit();
            if(count%100 == 0)
            {
                ElapsedTimer.elapsedTime(0);
                log.info(ElapsedTimer.buildMessageForElapsed(null, 0, 1));
            }
        }while(true);
    }

    public static void main(String[] args)
    {
        new ThrottleTest().start();
    }

    public class ConsumerTask extends Thread
    {
        @Override
        public void run()
        {

        }
    }

    public class Producer
    {
        private final static int PerSecond = 1000;
        private final Timer timer;
        private final AtomicInteger hits;

        public Producer()
        {
            hits = new AtomicInteger(0);
            timer = new Timer();
            timer.scheduleAtFixedRate(new ProducerTask(hits, PerSecond), PerSecond, PerSecond);
        }
        public void hit()
        {
            hits.incrementAndGet();
        }
    }

    public class ProducerTask extends TimerTask
    {
        private final AtomicInteger hits;
        private final int max;

        public ProducerTask(AtomicInteger hits, int max)
        {
            this.hits = hits;
            this.max = max;
        }

        @Override
        public void run()
        {
            int hitcount = hits.get();
            if(hitcount > max)
                log.warn("exceeded limit hits: " + hitcount + " max: " + max);
            else
                log.warn("producer hits: " + hitcount);
            hits.set(0);
        }
    }
}
