package xdman;

public class TestSystemDark {
    public static void main(String[] args) {
        try {
            // Use our own robust utility
            boolean isDark = xdman.util.XDMUtils.isSystemDark();
            System.out.println("XDMUtils.isSystemDark(): " + isDark);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
