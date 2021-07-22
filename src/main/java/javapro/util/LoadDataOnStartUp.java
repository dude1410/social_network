//package javapro.util;
//
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class LoadDataOnStartUp {
// Runnable task = () -> {
//         for (int i = 0; i < 100; i++) {
//        System.out.println("SecurityContextHolder" + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
//
//        try {
//        Thread.sleep(1000);
//        } catch (InterruptedException e) {
//        e.printStackTrace();
//        }
//        }
//        };
//        Thread thread = new Thread(task);
//        thread.start();
//



//    @EventListener(ApplicationReadyEvent.class)
//    public void loadData()
//    {
//        for(int i = 0; i< 100; i++){
//            System.out.println("SecurityContextHolder" + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
