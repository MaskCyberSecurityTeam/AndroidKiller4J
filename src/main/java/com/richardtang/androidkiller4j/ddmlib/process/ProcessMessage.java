package com.richardtang.androidkiller4j.ddmlib.process;

import lombok.Data;

/**
 * 进程消息实体类
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