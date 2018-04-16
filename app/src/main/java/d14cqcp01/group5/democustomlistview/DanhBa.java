package d14cqcp01.group5.democustomlistview;

public class DanhBa {

    private String phone;
    private String hoten;
    private String avartar;

    public DanhBa() {
    }

    public DanhBa(String phone, String hoten, String avartar) {
        this.phone = phone;
        this.hoten = hoten;
        this.avartar = avartar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getAvartar() {
        return avartar;
    }

    public void setAvartar(String avartar) {
        this.avartar = avartar;
    }
}
