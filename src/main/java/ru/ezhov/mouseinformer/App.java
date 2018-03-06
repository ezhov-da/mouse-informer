package ru.ezhov.mouseinformer;

import javax.swing.*;
import java.awt.*;
import java.awt.im.InputContext;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

public class App {
    private static final Logger LOG = Logger.getLogger(App.class.getName());

    /**
     * Да, да, да, здесь не код, а вырви глаз,
     * но приложения писалость просто для "попробовать"
     */
    public static void main(String[] args) {
        JWindow jWindow = new JWindow();
        JLabel label = new JLabel("1111111111");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jWindow.add(label);
        jWindow.setAlwaysOnTop(true);
        jWindow.pack();

        String s = ManagementFactory.getRuntimeMXBean().getName();
        if (!s.contains("@")) {
            JOptionPane.showMessageDialog(null, "Error get PID", "OOPS", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } else {
            try {
                Files.write(new File("pid.txt").toPath(), s.substring(0, s.indexOf("@")).getBytes());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Not write PID to file pid.txt", "OOPS", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(-2);
            }
        }

        Thread thread = new Thread(() -> {
            jWindow.setVisible(true);

            Point lastPoint = null;
            String lastDate = null;

            while (true) {
                try {

                    PointerInfo pointerInfo = MouseInfo.getPointerInfo();

                    if (pointerInfo == null) {
                        continue;
                    }

                    Point point = MouseInfo.getPointerInfo().getLocation();


                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!point.equals(lastPoint)) {

                        jWindow.setLocation(point.x + 10, point.y + 10);
                        lastPoint = point;
                    }

                    String date = new SimpleDateFormat("HH:mm:ss").format(new Date());

                    if (!date.equals(lastDate)) {

//                    String c = getCurrentKeyboardLayout();
//                    System.out.println(c);

                        SwingUtilities.invokeLater(() -> {
                            label.setText(date);
                        });

                        lastDate = date;
                    }

                } catch (NullPointerException e) {
                    //ignore
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static String getCurrentKeyboardLayout() {
        InputContext.getInstance().dispose();
        InputContext.getInstance().endComposition();
        InputContext instance = InputContext.getInstance();
        Object o = instance.getInputMethodControlObject();
        if (o != null) {
            System.out.println(o.getClass().getName());
        }
        instance.endComposition();
        Class<? extends InputContext> instanceClass = instance.getClass();
        Class<?> superclass = instanceClass.getSuperclass();
        if (superclass.getName().equals("sun.awt.im.InputContext")) {
            try {

                Field field = superclass.getDeclaredField("inputMethodLocator");
                field.setAccessible(true);
                Object inputMethodLocator = field.get(instance);


                Class aClass = inputMethodLocator.getClass();
                Field field1 = aClass.getDeclaredField("locale");
                field1.setAccessible(true);
                Locale locale = (Locale) field1.get(inputMethodLocator);

                return locale.getLanguage().toUpperCase(Locale.getDefault());
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
