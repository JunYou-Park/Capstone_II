package blog.practice.capstone_ii.DataVO;

public class UserVO {

    private String token, pw, name, type, phone;
    private int num4000, num3000, num2000;

    public UserVO(){
    }

    public UserVO(String token, String pw) {
        this.token = token;
        this.pw = pw;
    }

    public UserVO(String type, String token, String name) {
        this.type = type;
        this.token = token;
        this.name = name;
    }

    public UserVO(String token, String pw, String type, String phone) {
        this.token = token;
        this.pw = pw;
        this.type = type;
        this.phone = phone;
    }

    public UserVO(String type, String token, String pw, String name, String phone) {
        this.token = token;
        this.pw = pw;
        this.name = name;
        this.type = type;
        this.phone = phone;
    }

    public UserVO(String token, int num4000, int num3000, int num2000) {
        this.token = token;
        this.num4000 = num4000;
        this.num3000 = num3000;
        this.num2000 = num2000;
    }

    public int getNum4000() {
        return num4000;
    }

    public void setNum4000(int num4000) {
        this.num4000 = num4000;
    }

    public int getNum3000() {
        return num3000;
    }

    public void setNum3000(int num3000) {
        this.num3000 = num3000;
    }

    public int getNum2000() {
        return num2000;
    }

    public void setNum2000(int num2000) {
        this.num2000 = num2000;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
