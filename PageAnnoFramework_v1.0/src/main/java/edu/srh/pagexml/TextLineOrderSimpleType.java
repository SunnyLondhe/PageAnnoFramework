//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.24 at 10:01:57 AM CEST 
//


package edu.srh.pagexml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TextLineOrderSimpleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TextLineOrderSimpleType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="top-to-bottom"/>
 *     &lt;enumeration value="bottom-to-top"/>
 *     &lt;enumeration value="left-to-right"/>
 *     &lt;enumeration value="right-to-left"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TextLineOrderSimpleType")
@XmlEnum
public enum TextLineOrderSimpleType {

    @XmlEnumValue("top-to-bottom")
    TOP_TO_BOTTOM("top-to-bottom"),
    @XmlEnumValue("bottom-to-top")
    BOTTOM_TO_TOP("bottom-to-top"),
    @XmlEnumValue("left-to-right")
    LEFT_TO_RIGHT("left-to-right"),
    @XmlEnumValue("right-to-left")
    RIGHT_TO_LEFT("right-to-left");
    private final String value;

    TextLineOrderSimpleType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TextLineOrderSimpleType fromValue(String v) {
        for (TextLineOrderSimpleType c: TextLineOrderSimpleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}