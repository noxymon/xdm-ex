package xdman;

public class TestTheme {
    public static void main(String[] args) {
        try {
            // Check if our own utility works
            boolean isDark = xdman.util.XDMUtils.isSystemDark();
            System.out.println("XDMUtils.isSystemDark(): " + isDark);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
