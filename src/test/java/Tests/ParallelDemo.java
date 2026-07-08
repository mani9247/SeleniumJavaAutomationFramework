package Tests;

import Base.BaseTest;
import org.testng.annotations.Test;

public class ParallelDemo extends BaseTest{
        @Test
        public void testOne() throws InterruptedException {

            System.out.println(Thread.currentThread().getId());

            Thread.sleep(5000);
        }

        @Test
        public void testTwo() throws InterruptedException {

            System.out.println(Thread.currentThread().getId());

            Thread.sleep(5000);
        }

        @Test
        public void testThree() throws InterruptedException {

            System.out.println(Thread.currentThread().getId());

            Thread.sleep(5000);
        }

    }
