package com.richardtang.androidkiller4j.task.watch;

import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 文件监测任务基类
 *
 * @author RichardTang
 */
public abstract class BaseFileWatchTask extends WatchMonitor {

    protected BaseFileWatchTask(File watchDirectory) {
        super(watchDirectory, WatchMonitor.EVENTS_ALL);
        setMaxDepth(getMaxDepth());
        setWatcher(getSimpleWatcher());
    }

    /**
     * 基于hutool的SimpleWatcher实现文件的监测
     *
     * @return hutool的SimpleWatcher
     */
    protected SimpleWatcher getSimpleWatcher() {
        return new SimpleWatcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                BaseFileWatchTask.this.onCreate(event, currentPath);
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                BaseFileWatchTask.this.onModify(event, currentPath);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                BaseFileWatchTask.this.onDelete(event, currentPath);
            }
        };
    }

    /**
     * 配置监测的目录深度，默认为1层，子类可通过覆盖函数进行修改。
     *
     * @return 目录深度
     */
    protected int getMaxDepth() {
        return 1;
    }

    /**
     * 开启文件监测
     */
    @Override
    public synchronized void start() {
        super.start();
        startAfter();
    }

    /**
     * 启用文件监测后的回调函数
     */
    protected void startAfter() {

    }

    /**
     * 有新文件创建时的回到函数
     *
     * @param watchEvent 监测事件
     * @param path       触发回调函数的路径
     */
    protected void onCreate(WatchEvent<?> watchEvent, Path path) {

    }

    /**
     * 有文件被修改时的回到函数
     *
     * @param watchEvent 监测事件
     * @param path       触发回调函数的路径
     */
    protected void onModify(WatchEvent<?> watchEvent, Path path) {

    }

    /**
     * 有文件被删除时的回调函数
     *
     * @param watchEvent 监测事件
     * @param path       触发回调函数的路径
     */
    protected void onDelete(WatchEvent<?> watchEvent, Path path) {

    }
}