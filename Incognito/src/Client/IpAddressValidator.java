package Client;

public class IpAddressValidator {

    private final String regex = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";

    public boolean isValid(String address) {
        if (address.matches(regex)) {
            String[] segments = address.split("\\.");
            for (String segment : segments) {
                if (Integer.parseInt(segment) > 255 || Integer.parseInt(segment) < 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
