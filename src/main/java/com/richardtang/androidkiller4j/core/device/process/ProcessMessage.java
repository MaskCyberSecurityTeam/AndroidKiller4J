package com.richardtang.androidkiller4j.core.device.process;

import lombok.Data;

/**
 * 用于存储，Android设备中Top命令的每一项信息。
 *
 * @author RichardTang
 */
@Data
public class ProcessMessage {
    private String pid;
    private String user;
    private String pr;
    private String ni;
    private String virt;
    private String res;
    private String shr;
    private String s;
    private String cpu;
    private String mem;
    private String time;
    private String args;
}