package com.richardtang.androidkiller4j.view.device;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.android.ddmlib.logcat.LogCatMessage;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Data
@Component
public class DeviceLogView extends JPanel implements InitializingBean {

    // 条件查询组件
    private JLabel     filterLabel;
    private JTextField filterTextField;
    private Box        conditionQueryHBox;

    // 开始/暂停按钮
    private JButton changeButton;

    // 表格组件
    private JTable                                table;
    private DefaultEventTableModel<LogCatMessage> tableModel;
    private EventList<LogCatMessage>              tableEventList; // 加入数据的容器
    private FilterList<LogCatMessage>             tableFilteredList; // 实际显示数据的容器 主要辅助进行数据筛选
    private TextFilterator<LogCatMessage>         tableTextFilterator;
    private MatcherEditor<LogCatMessage>          tableMatcherEditor;

    @Autowired
    private DeviceLogViewAction deviceLogViewEvent;

    private void renderConditionComponent() {
        filterLabel     = new JLabel("过滤: ");
        filterTextField = new JTextField();
        changeButton    = new JButton("开始");

        conditionQueryHBox = Box.createHorizontalBox();
        conditionQueryHBox.add(filterLabel);
        conditionQueryHBox.add(filterTextField);
        conditionQueryHBox.add(changeButton);
        conditionQueryHBox.setBorder(new EmptyBorder(3, 2, 0, 2));

        add(conditionQueryHBox, BorderLayout.NORTH);
    }

    private void renderJTable() {
        // 初始化过滤用
        tableTextFilterator = (baseList, logCatMessage) -> {
            baseList.add(logCatMessage.getAppName());
            baseList.add(logCatMessage.getMessage());
            baseList.add(logCatMessage.getTag());
            baseList.add(String.valueOf(logCatMessage.getTid()));
            baseList.add(String.valueOf(logCatMessage.getPid()));
            baseList.add(logCatMessage.getHeader().toString());
            baseList.add(logCatMessage.getLogLevel().getStringValue());
        };

        tableMatcherEditor = new TextComponentMatcherEditor<>(filterTextField, tableTextFilterator);
        tableEventList     = new BasicEventList<>();
        tableFilteredList  = new FilterList<>(tableEventList, tableMatcherEditor);
        tableModel         = new DefaultEventTableModel<>(tableFilteredList, new TableFormat<LogCatMessage>() {
            @Override
            public int getColumnCount() {
                return 7;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "应用名称";
                    case 1:
                        return "消息";
                    case 2:
                        return "级别";
                    case 3:
                        return "头信息";
                    case 4:
                        return "标签";
                    case 5:
                        return "TID";
                    case 6:
                        return "时间";
                }
                throw new IllegalStateException("Unexpected column: " + column);
            }

            @Override
            public Object getColumnValue(LogCatMessage logCatMessage, int column) {
                switch (column) {
                    case 0:
                        return logCatMessage.getAppName();
                    case 1:
                        return logCatMessage.getMessage();
                    case 2:
                        return logCatMessage.getLogLevel().getStringValue();
                    case 3:
                        return logCatMessage.getHeader();
                    case 4:
                        return logCatMessage.getTag();
                    case 5:
                        return logCatMessage.getTid();
                    case 6:
                        return logCatMessage.getTimestamp();
                }
                throw new IllegalStateException("Unexpected column: " + column);
            }
        });
        table              = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initEvent() {
        changeButton.addActionListener(event -> deviceLogViewEvent.changeButtonOnMouseClick(event));
    }

    @Override
    public void afterPropertiesSet() {
        setLayout(new BorderLayout());

        renderConditionComponent();
        renderJTable();
        initEvent();
    }
}