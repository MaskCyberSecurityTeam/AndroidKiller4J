package com.richardtang.androidkiller4j.view.device;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.richardtang.androidkiller4j.core.device.process.ProcessMessage;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Data
@Component
public class DeviceProcessView extends JPanel implements InitializingBean {

    // 条件查询组件
    private JLabel     filterLabel;
    private JTextField filterTextField;
    private Box        conditionQueryHBox;

    // 开始/暂停按钮
    private JButton changeButton;

    // 表格组件
    private JTable                                 table;
    private DefaultEventTableModel<ProcessMessage> tableModel;
    private EventList<ProcessMessage>              tableEventList; // 加入数据的容器
    private FilterList<ProcessMessage>             tableFilteredList; // 实际显示数据的容器 主要辅助进行数据筛选
    private TextFilterator<ProcessMessage>         tableTextFilterator;
    private MatcherEditor<ProcessMessage>          tableMatcherEditor;

    @Autowired
    private DeviceProcessViewAction deviceProcessViewEvent;

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
        tableTextFilterator = (baseList, processMessage) -> {
            baseList.add(processMessage.getPid());
            baseList.add(processMessage.getUser());
            baseList.add(processMessage.getPr());
            baseList.add(processMessage.getNi());
            baseList.add(processMessage.getVirt());
            baseList.add(processMessage.getRes());
            baseList.add(processMessage.getS());
            baseList.add(processMessage.getCpu());
            baseList.add(processMessage.getMem());
            baseList.add(processMessage.getTime());
            baseList.add(processMessage.getArgs());
        };

        tableMatcherEditor = new TextComponentMatcherEditor<>(filterTextField, tableTextFilterator);
        tableEventList     = new BasicEventList<>();
        tableFilteredList  = new FilterList<>(tableEventList, tableMatcherEditor);
        tableModel         = new DefaultEventTableModel<>(tableFilteredList, new TableFormat<ProcessMessage>() {
            @Override
            public int getColumnCount() {
                return 11;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "PID";
                    case 1:
                        return "用户";
                    case 2:
                        return "PR";
                    case 3:
                        return "NI";
                    case 4:
                        return "VIRT";
                    case 5:
                        return "SHR";
                    case 6:
                        return "S";
                    case 7:
                        return "CPU使用率";
                    case 8:
                        return "内存占用";
                    case 9:
                        return "时间";
                    case 10:
                        return "参数值";
                }
                throw new IllegalStateException("Unexpected column: " + column);
            }

            @Override
            public Object getColumnValue(ProcessMessage processMessage, int column) {
                switch (column) {
                    case 0:
                        return processMessage.getPid();
                    case 1:
                        return processMessage.getUser();
                    case 2:
                        return processMessage.getPr();
                    case 3:
                        return processMessage.getNi();
                    case 4:
                        return processMessage.getVirt();
                    case 5:
                        return processMessage.getShr();
                    case 6:
                        return processMessage.getS();
                    case 7:
                        return processMessage.getCpu();
                    case 8:
                        return processMessage.getMem();
                    case 9:
                        return processMessage.getTime();
                    case 10:
                        return processMessage.getArgs();
                }
                throw new IllegalStateException("Unexpected column: " + column);
            }
        });

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initEvent() {
        changeButton.addActionListener(event -> deviceProcessViewEvent.changeButtonOnMouseClick(event));
    }

    @Override
    public void afterPropertiesSet() {
        setLayout(new BorderLayout());

        renderConditionComponent();
        renderJTable();
        initEvent();
    }
}