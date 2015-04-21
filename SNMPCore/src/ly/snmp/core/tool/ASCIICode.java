package ly.snmp.core.tool;
/*
 * ASCIICode.java
 * Date: 2/28/2015
 * Time: 11:18 AM
 */

public enum ASCIICode {
    NUL(0, "00"),
    SOH(1, "01"),
    STX(2, "02"),
    ETX(3, "03"),
    EOT(4, "04"),
    ENQ(5, "05"),
    ACK(6, "06"),
    BEL(7, "07"),
    BS(8, "08"),
    HT(9, "09"),
    LF(10, "0A"),
    VT(11, "0B"),
    FF(12, "0C"),
    CR(13, "0D"),
    SO(14, "0E"),
    SI(15, "0F"),
    DLE(16, "10"),
    DC1(17, "11"),
    DC2(18, "12"),
    DC3(19, "13"),
    DC4(20, "14"),
    NAK(21, "15"),
    SYN(22, "16"),
    TB(23, "17"),
    CAN(24, "18"),
    EM(25, "19"),
    SUB(26, "1A"),
    ESC(27, "1B"),
    FS(28, "1C"),
    GS(29, "1D"),
    RS(30, "1E"),
    US(31, "1F"),
    DEL(127, "7F"),
    SPACE(32, "20"),
    NO_CONTROL(-1, "FF");
    private int Decimal_Code;
    private String Hexadecimal_Code;

    private ASCIICode(int decimalCode, String hexadecimalCode) {
        this.Decimal_Code = decimalCode;
        this.Hexadecimal_Code = hexadecimalCode;
    }

    public static ASCIICode getASCII(int decimalCode) {
        for (ASCIICode code : values()) {
            if (code.getDecimal_Code() == decimalCode) {
                return code;
            }
        }
        return NO_CONTROL;
    }

    public String getHexadecimal_Code() {
        return Hexadecimal_Code;
    }

    public int getDecimal_Code() {
        return Decimal_Code;
    }
}
