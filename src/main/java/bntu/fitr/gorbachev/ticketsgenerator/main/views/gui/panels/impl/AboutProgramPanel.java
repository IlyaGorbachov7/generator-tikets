package bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.tools.FileNames;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AboutProgramPanel extends BasePanel {

    public AboutProgramPanel(Window frame) {
        super(frame);
        this.initPanel();
    }

    @Override
    public void initPanel() {
        this.setLayout(new BorderLayout());
        JLabel lbHtml = new JLabel(
                """
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <title>Ilya Gorbachev</title>
                        </head>

                        <body>\s
                            <div style="text-align: center;">
                                <h1>Конструктор экзаменационных билетов</h1>
                                <hr>
                            </div>
                           \s
                            <div style="text-indent: 5px; margin-left: 10px; font-size: 12px;">
                               <div>
                                    <p>Программа позволяет сформировать список экзаменационных билетов <br>\s
                                    представленного в виде файла <strong><em>.docx</em></strong>.
                                </p>
                                <p style="margin-top: 0px;">
                                    Список вопросов необходимо предоставить файлом <strong><em>.docx</em></strong><br>
                                 </p>
                               </div>
                               <div  style="margin-top: 10px;">
                                 <p>  Чтобы из файла считать список вопросов, требуется применить тег: <strong>&lt;S&gt</strong></p>
                                 <p>Данный тег, определяет список вопросов.</p>
                                 <p>Он имеет начальный тег: <strong>&lt;S&gt</strong> и конечный тег: <strong>&lt;S/&gt</strong></p>\s
                                 <p>К начальному тегу можно применить следующую конструкцию: <strong>&lt;S&gt;topic&gt;</strong></p>
                                <p> Здесь topic - это название вашего раздела (указывать его не обязательно).<br>
                                    Он необходим лишь для того, чтобы в дальнейшем сгруппировать вопросы,<br>
                                    принадлежащие одному разделу, в один общий список. </p>
                            </div>
                            <p><hr></p>
                                 <div>
                                <p style="font-size: 12px;  margin-top: 10px; margin-bottom: 0%;" >
                                    <strong>Требования к оформлению абзаца на наличие тега</strong>
                                    <ul>
                                        <li>Теги пишется заглавными буквами;</li>
                                        <li>Абзац должен быть выравнен по центру;</li>
                                        <li>Абзац не должен содержать чего-либо другого, кроме тега &lt;S&gt; или &lt;S/&gt;</li>
                                    </ul>
                                </p>
                                </div>

                                <div>
                                    <p style="font-size: 12px;  margin-top: 10px; margin-bottom: 0%;" >
                                        <strong>Требования к оформлению списка вопросов</strong>
                                        <ul>
                                            <li>Список вопросов должен находится между начальным тегам &lt;S&gt; <br>
                                                 и конечным тегом &lt;S/&gt;;</li>
                                            <li>Каждый вопрос должен быть нумерован;</li>
                                            <li>После объявления открывающего тега &lt;S&gt с нового  абзаца должен <br>
                                                идти нумерованный список вопросов,после которого с нового абзаца<br>\s
                                                должен идти закрывающий тег &lt;S/&gt;;</li>
                                        </ul>
                                    </p>
                                    </div>
                                    <p><hr></p>
                                    <div>
                                        <p>Вопрос может содержать графическую информацию: рисунки, <br>
                                        математические уравнения, формулы.</p>
                                        <p>К вопросу будет относиться содержание от начало его нумерации до <br>следующего вопроса либо до конечного тега.</p>
                                       <p>К текстовому содержимому вопроса, могут быть применимы все виды <br>
                                                                 форматирования: <em>цвета, полужирный, курсив, зачеркивание,<br>
                                                                     подчеркивание, надстрочный подстрочный текст,</em> а также текстовые <br>
                                                                     эффекты, <em>такие как контур, тень, отражения </em>и другие.</p>
                                                             <p>К рисунку могут быть применимы все режимы параметры разметки:<br>
                                                                   <em>в тексте, обтеканием текстом.</em> В последнем случаи, рисунок будет <br>
                                                                   относится к тому вопросу, к какому привязан он абзацу, а также иметь такое <br>
                                                                   же расположение, какое было указана, при оформлении его в вопросе.
                                                                 </p>
                                </div>
                                <div>
                                    <p style="margin-bottom: 20px;">Пример списка вопросов</p>
                                    <p> </p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """);
        lbHtml.setFont(new Font("Serif", Font.PLAIN, 14));
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));

        JPanel panelHtml = new JPanel(new GridLayout(1, 1, 5, 5));
        panelHtml.add(lbHtml);
        panelInfo.add(panelHtml);

        JLabel lbImage = new JLabel(new ImageIcon(Objects.requireNonNull(
                FileNames.getResource(FileNames.picturePrim1))
        ));
        ;
        JPanel panelImage = new JPanel(new GridLayout(1, 1, 5, 5));
        panelImage.add(lbImage);
        panelInfo.add(panelImage);

        this.add(new JScrollPane(panelInfo), BorderLayout.CENTER);

        var btnOk = new JButton("OK");
        btnOk.addActionListener(event -> getRootFrame().setVisible(false));
        add(btnOk, BorderLayout.SOUTH);
    }

    @Override
    public void setComponentsListeners() {

    }

    @Override
    public void setConfigComponents() {

    }

    @Override
    public void changeStateViewElems(ChangeFieldModelEvent event) {

    }
}
