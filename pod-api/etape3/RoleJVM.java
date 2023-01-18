public class RoleJVM {


    private static boolean isServer = false;

    public static boolean isServer() {
        return isServer;
    }

    public static void setServer(boolean isServer) {
        RoleJVM.isServer = isServer;
    }
    
}
