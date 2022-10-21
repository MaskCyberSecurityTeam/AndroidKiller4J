/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.richardtang.androidkiller4j.ddmlib.process;

import java.util.List;

/**
 * 进程消息结果处理接口
 *
 * @author RichardTang
 */
public interface ProcessMessageListener {

    /**
     * 定义处理Top命令执行的结果处理方式
     *
     * @param msgList top命令产生的每一行数据都会被封装为一个元素并存如集合中
     */
    void log(List<ProcessMessage> msgList);
}