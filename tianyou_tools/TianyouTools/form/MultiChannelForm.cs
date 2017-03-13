using System;
using System.Windows.Forms;
using System.Xml;
using System.Collections;
using System.IO;
using System.Text;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Threading;

namespace TianyouMultiChannel
{
    public partial class MultiChannelForm : Form
    {
        private String apkPath;       //主包路径
        private String apkName;       //主包名
        private String channel;       //选中的渠道

        private ArrayList paramList = new ArrayList();      //sdk参数集合
        private ArrayList payParamList = new ArrayList();   //支付参数集合
        private int a = 0, b = 0;
        private Button addPayBtn;
        private String packageName;     //包名
        private ChannelParam.Result.Channelinfo channelinfo;
        private String channelJson;
        private String payJson;

        public MultiChannelForm()
        {
            InitializeComponent();
            getChannelParamList();
        }

        private void getChannelParamList()
        {
            String json = HttpUtils.post("http://192.168.1.169/morechannel/index.php/MoreChannel/getgames", null);
            DataContractJsonSerializer js = new DataContractJsonSerializer(typeof(ChannelList));
            using (MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(json)))
            {
                ChannelList list = js.ReadObject(ms) as ChannelList;
                if (list.result.code == 200)
                {
                    ChannelList.Result.Gameinfo[] gameInfo = list.result.gameinfo;
                    for (int i = 0; i < gameInfo.Length; i++)
                    {
                        TreeNode node = new TreeNode();
                        node.Text = gameInfo[i].name;
                        treeView1.Nodes.Add(node);
                        ChannelList.Result.Gameinfo.Second[] second = gameInfo[i].second;
                        if (second != null)
                        {
                            for (int j = 0; j < second.Length; j++)
                            {
                                TreeNode cNode = new TreeNode();
                                cNode.Text = second[j].name;
                                cNode.Name = second[j].id;
                                node.Nodes.Add(cNode);
                            }
                        }
                    }
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            saveJsonData("channel_info.json", channelJson);
            saveJsonData("pay_info.json", payJson);
            if (textBox2.Text.Length == 0)
            {
                MessageBox.Show("请先上传游戏主包！", "提示", MessageBoxButtons.OK);
                return;
            }
            if (channelJson == null)
            {
                MessageBox.Show("请先选择渠道", "提示", MessageBoxButtons.OK);
                return;
            }
            if (!Directory.Exists("package"))
            {
                Directory.CreateDirectory("package");
            }
            new Thread(doPackingAPK).Start();
        }

        private void saveJsonData(String fileName, String data)
        {
            if (!Directory.Exists("temp"))
            {
                Directory.CreateDirectory("temp");
            }
            FileStream fs = new FileStream("temp\\" + fileName, FileMode.Create);
            StreamWriter sw = new StreamWriter(fs);
            try
            {
                sw.Write(data);
                sw.Flush();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message.ToString());
            }
            finally
            {
                sw.Close();
                fs.Close();
            }
        }

        //开始打包
        private void doPackingAPK()
        {
            ApkUtils apkUtils = new ApkUtils(logList);
            //11.删除临时文件
            //apkUtils.deleteFile(apkName + " temp out");
            //1.解包命令
            apkUtils.unApk(apkPath);
            //2.复制assets命令
            apkUtils.copyFile("sdk_res\\" + channel + "\\assets", apkName + "\\assets");
            apkUtils.copyFile("temp", apkName + "\\assets");
            //3.修改Manifest文件
            updateManifest();
            //4.复制res命令
            apkUtils.copyFile("sdk_res\\" + channel + "\\res", apkName + "\\res");
            //4.复制多渠道jar包命令
            //apkUtils.copyFile("sdk_res\\*.jar" + channel + "\\res", "sdk_res\\" + channel + "\\libs");
            //5.jar to dex

            apkUtils.jar2dex("sdk_res\\*.jar");
            //6.dex to smali
            apkUtils.dex2smali();

            //5.jar to dex
            apkUtils.jar2dex("sdk_res\\" + channel + "\\libs\\*.jar");
            //6.dex to smali
            apkUtils.dex2smali();

            //7.复制smali文件
            apkUtils.copyFile("out", apkName + "\\smali");
            //8.合包
            apkUtils.joinApk(apkName);
            //9.签名
            apkUtils.signApk(apkName);
            //10.优化
            apkUtils.alignApk(apkName, "package\\" + apkName + "_" + channel);
            //11.删除临时文件
            //apkUtils.deleteFile(apkName + " temp out classes.dex");
            //MessageBox.Show("打包完成！", "提示", MessageBoxButtons.OK);
        }

        //修改清单文件
        private void updateManifest()
        {
            ArrayList permissionList = new ArrayList();
            XmlDocument xmlDoc = new XmlDocument();
            try
            {
                xmlDoc.Load("sdk_res\\" + channel + "\\AndroidManifest.xml");
            }
            catch
            {
                showLog("路径错误");
                return;
            }
            showLog("sdk_res\\" + channel + "\\AndroidManifest.xml");
            XmlNode manifest = xmlDoc.SelectSingleNode("manifest");
            XmlNodeList uses_permission = manifest.SelectNodes("uses-permission");
            foreach (XmlNode node in uses_permission)
            {
                showLog("uses_permission=" + node.Attributes["android:name"].Value);
                permissionList.Add(node.Attributes["android:name"].Value);
            }
            XmlNode application = manifest.SelectSingleNode("application");
            try
            {
                XmlNodeList childNode = application.ChildNodes;
                writeParseXml(permissionList, childNode);
            }
            catch
            {
                showLog("application没有字节点");
                writeParseXml(permissionList, null);
            }
        }

        private void writeParseXml(ArrayList permissionList, XmlNodeList childNode)
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(apkName + "\\AndroidManifest.xml");
            XmlNode manifest = xmlDoc.SelectSingleNode("manifest");
            if (packageName != null)
                manifest.Attributes["package"].Value = packageName;
            string w3NameSpace = "http://schemas.android.com/apk/res/android";
            foreach (String str in permissionList)
            {
                XmlElement element = xmlDoc.CreateElement("uses-permission");
                showLog("插入权限: " + str + "\r\n");
                XmlAttribute attribute = xmlDoc.CreateAttribute("android", "name", w3NameSpace);
                attribute.Value = str;
                element.SetAttributeNode(attribute);
                manifest.AppendChild(element);
            }
            XmlNode application = manifest.SelectSingleNode("application");
            if (childNode != null)
            {
                foreach (XmlNode node in childNode)
                {
                    XmlNode newNode = xmlDoc.ImportNode(node, true);
                    application.AppendChild(newNode);
                }
                xmlDoc.Save(apkName + "\\AndroidManifest.xml");
            }
        }

        private String getSystemTime()
        {
            System.DateTime currentTime = new System.DateTime();
            currentTime = System.DateTime.Now;
            return "" + currentTime.Year + currentTime.Month + currentTime.Day + currentTime.Hour + currentTime.Minute + currentTime.Second;
        }

        private void button4_Click(object sender, EventArgs e)
        {
            ApkToolsForm form = new ApkToolsForm();
            form.Show();
        }

        private void textBox2_DragEnter(object sender, DragEventArgs e)
        {
            string path = ((System.Array)e.Data.GetData(DataFormats.FileDrop)).GetValue(0).ToString();
            textBox2.Text = path;
            logList.AppendText("path: " + path + "\r\n");
            apkPath = path;
            String safeName = path.Substring(path.LastIndexOf("\\") + 1);
            apkName = safeName.Substring(0, safeName.LastIndexOf("."));
        }

        private void listBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            loadChannelConfig();
        }

        private void loadChannelConfig() 
        {
            logList.AppendText("选择渠道: " + channel + "\r\n");
            panel1.Controls.Clear();
            panel2.Controls.Clear();
            a = 0; b = 0;
            payParamList.Clear();
            readChannelConfig();
        }

        //读取渠道配置信息
        private void readChannelConfig()
        {
            XmlDocument xmlDoc = new XmlDocument();
            try
            {
                xmlDoc.Load("sdk_res\\" + channel + "\\assets\\channel_config.xml");
            }
            catch
            {
                MessageBox.Show("渠道配置文件不存在！", "提示", MessageBoxButtons.OK);
                return;
            }
            XmlNode infos = xmlDoc.SelectSingleNode("infos");
            XmlNode channelNode = infos.SelectSingleNode(channel);
            XmlNodeList childNode = channelNode.ChildNodes;
            
            foreach (XmlNode node in childNode)
            {
                if (node.Name.Equals("package_name"))
                    packageName = node.InnerText;

                paramList.Add(node.Name);

                Label paramName = new Label();
                paramName.AutoSize = true;
                paramName.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                paramName.Text = node.Name + ":";
                
                TextBox paramValue = new TextBox();
                paramValue.Size = new System.Drawing.Size(300, 20);
                paramValue.Name = node.Name;
                paramValue.Text = node.InnerText;

                if (node.Name != "pay_info")
                {
                    paramName.Location = new System.Drawing.Point(10, 30 * a);
                    paramValue.Location = new System.Drawing.Point(130, 30 * a);
                    panel1.Controls.Add(paramName);
                    panel1.Controls.Add(paramValue);
                    a++;
                }
                else
                {
                    XmlNodeList payChildNode = node.ChildNodes;
                    String payCode = "";     //当前计费id
                    foreach (XmlNode cNode in payChildNode)
                    {
                        if (cNode.Name.Equals("id"))
                            payCode = cNode.InnerText;
                        if (!payParamList.Contains(cNode.Name))
                            payParamList.Add(cNode.Name);
                        Label payParamName = new Label();
                        payParamName.AutoSize = true;
                        payParamName.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                        payParamName.Location = new System.Drawing.Point(10, 30 * b);
                        payParamName.Text = cNode.Name + ":";
                        panel2.Controls.Add(payParamName);

                        TextBox payParamValue = new TextBox();
                        payParamValue.Size = new System.Drawing.Size(290, 20);
                        payParamValue.Name = cNode.Name;
                        payParamValue.Location = new System.Drawing.Point(130, 30 * b);
                        payParamValue.Text = cNode.InnerText;
                        panel2.Controls.Add(payParamValue);
                        b++;
                    }
                    Button saveBtn = new Button();
                    saveBtn.Location = new System.Drawing.Point(120, 30 * b);
                    saveBtn.Size = new System.Drawing.Size(100, 30);
                    saveBtn.Text = "保存修改";
                    panel2.Controls.Add(saveBtn);
                    saveBtn.Click += new System.EventHandler(this.saveBtn_Click);

                    Button deleteBtn = new Button();
                    deleteBtn.Location = new System.Drawing.Point(240, 30 * b);
                    deleteBtn.Size = new System.Drawing.Size(100, 30);
                    deleteBtn.Text = "删除计费";
                    deleteBtn.Name = payCode;
                    deleteBtn.Click += new System.EventHandler(this.deleteBillingBtn_Click);
                    panel2.Controls.Add(deleteBtn);
                    b++;

                    Label lineLabel = new Label();
                    lineLabel.AutoSize = true;
                    lineLabel.Location = new System.Drawing.Point(0, 30 * b++);
                    lineLabel.Text = "----------------------------------------------------";
                    panel2.Controls.Add(lineLabel);
                }
            }
            Button btn1 = new Button();
            btn1.Location = new System.Drawing.Point(180, 30 * a + 10);
            btn1.Size = new System.Drawing.Size(100, 30);
            btn1.Text = "保存修改";
            panel1.Controls.Add(btn1);
            btn1.Click += new System.EventHandler(this.saveBtn_Click);

            addPayBtn = new Button();
            addPayBtn.Location = new System.Drawing.Point(10, 30 * --b + 20);
            addPayBtn.Size = new System.Drawing.Size(100, 30);
            addPayBtn.Text = "添加计费点";
            panel2.Controls.Add(addPayBtn);
            addPayBtn.Click += new System.EventHandler(this.addBillingPoint_Click);
        }

        //删除计费点
        private void deleteBillingBtn_Click(object sender, EventArgs e)
        {
            String payCode = ((Button)sender).Name;
            showLog("payCode:" + payCode);
            XmlDocument xmlDoc = new XmlDocument();
            try
            {
                xmlDoc.Load("sdk_res\\" + channel + "\\assets\\channel_config.xml");
            }
            catch
            {
                MessageBox.Show("渠道配置文件不存在！", "提示", MessageBoxButtons.OK);
                return;
            }
            XmlNode infos = xmlDoc.SelectSingleNode("infos");
            XmlNode channelNode = infos.SelectSingleNode(channel);
            XmlNodeList childNode = channelNode.ChildNodes;
            foreach (XmlNode node in childNode)
            {
                if (node.Name == "pay_info")
                {
                    showLog("---" + node.SelectSingleNode("id").InnerText);
                    if (payCode.Equals(node.SelectSingleNode("id").InnerText))
                        channelNode.RemoveChild(node);
                }
            }
            xmlDoc.Save("sdk_res\\" + channel + "\\assets\\channel_config.xml");
            loadChannelConfig();
        }

        //增加计费点信息
        private void addBillingPoint_Click(object sender, EventArgs e)
        {
            int c = 0;
            foreach (String payParam in payParamList)
            {
                Label payParamName = new Label();
                payParamName.AutoSize = true;
                payParamName.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                payParamName.Text = payParam + ":";

                TextBox payParamValue = new TextBox();
                payParamValue.Size = new System.Drawing.Size(290, 20);
                payParamValue.Name = payParam;

                payParamName.Location = new System.Drawing.Point(10, addPayBtn.Location.Y + 30 * c);
                payParamValue.Location = new System.Drawing.Point(130, addPayBtn.Location.Y + 30 * c);
                panel2.Controls.Add(payParamName);
                panel2.Controls.Add(payParamValue);
                c++;
            }
            Button saveBtn = new Button();
            saveBtn.Location = new System.Drawing.Point(120, addPayBtn.Location.Y + 30 * c);
            saveBtn.Size = new System.Drawing.Size(100, 30);
            saveBtn.Text = "保存修改";
            panel2.Controls.Add(saveBtn);
            saveBtn.Click += new System.EventHandler(this.saveBtn_Click);

            Button deleteBtn = new Button();
            deleteBtn.Location = new System.Drawing.Point(240, addPayBtn.Location.Y + 30 * c);
            deleteBtn.Size = new System.Drawing.Size(100, 30);
            deleteBtn.Text = "删除计费";
            panel2.Controls.Add(deleteBtn);
            deleteBtn.Click += new System.EventHandler(this.saveBtn_Click);
            c++;

            Label lineLabel = new Label();
            lineLabel.AutoSize = true;
            lineLabel.Location = new System.Drawing.Point(0, addPayBtn.Location.Y + 30 * c++);
            lineLabel.Text = "----------------------------------------------------";
            panel2.Controls.Add(lineLabel);

            addPayBtn.Location = new System.Drawing.Point(10, addPayBtn.Location.Y + 30 * c);
        }

        private void saveBtn_Click(object sender, EventArgs e)
        {
            XmlDocument xmlDoc = new XmlDocument();
            try
            {
                xmlDoc.Load("sdk_res\\" + channel + "\\assets\\channel_config.xml");
            }
            catch
            {
                MessageBox.Show("渠道配置文件不存在！", "提示", MessageBoxButtons.OK);
                return;
            }
            XmlNode infos = xmlDoc.SelectSingleNode("infos");
            XmlNode channelNode = infos.SelectSingleNode(channel);
            foreach (String paramName in paramList)
            {
                XmlNode node = channelNode.SelectSingleNode(paramName);
                node.InnerText = Controls.Find(paramName, true)[0].Text;
            }
            xmlDoc.Save("sdk_res\\" + channel + "\\assets\\channel_config.xml");
            MessageBox.Show("保存成功！", "提示", MessageBoxButtons.OK);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            logList.Clear();
            showLog("packageName:" + packageName);
        }

        private void 出包文件夹ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            System.Diagnostics.Process.Start("explorer.exe", "package");
        }

        delegate void SetTextCallback(string text);

        private void showLog(String log)
        {
            if (this.logList.InvokeRequired)
            {
                while (!this.logList.IsHandleCreated)
                {
                    //解决窗体关闭时出现“访问已释放句柄“的异常
                    if (this.logList.Disposing || this.logList.IsDisposed)
                        return;
                }
                SetTextCallback d = new SetTextCallback(showLog);
                this.logList.Invoke(d, new object[] { log });
            }
            else
            {
                logList.AppendText(log + "\r\n");
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog();
            if (dialog.ShowDialog() == DialogResult.OK)
            {
                apkPath = dialog.FileName;
                String safeName = dialog.SafeFileName;
                apkName = safeName.Substring(0, safeName.LastIndexOf("."));
                textBox2.Text = dialog.FileName;
            }
        }

        private void treeView1_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
        {
            if (e.Node.Bounds.Contains(e.Location) && e.Node.Nodes.Count == 0)
            {
                getPayParamList();

                treeView1.SelectedNode = e.Node;
                channel = e.Node.Text;
                showLog(channel);
                String channelId = e.Node.Name;

                Hashtable map = new Hashtable();
                map.Add("channelid", channelId);
                //map.Add("channelid", channelId);
                channelJson = HttpUtils.post("http://192.168.1.169/morechannel/index.php/MoreChannel/getchannelinfo", map);

                DataContractJsonSerializer js = new DataContractJsonSerializer(typeof(ChannelParam));
                using (MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(channelJson)))
                {
                    ChannelParam param = js.ReadObject(ms) as ChannelParam;
                    if (param.result.code == 200)
                    {
                        channelinfo = param.result.channelinfo;
                        textBoxId.Text = channelinfo.channel_type;
                        textBoxName.Text = channelinfo.name;
                        textBoxAppId.Text = channelinfo.appid;
                        textBoxAppKey.Text = channelinfo.appkey;
                        textBoxAppSecret.Text = channelinfo.appsecret;
                        packageName = channelinfo.package;
                    }
                }
            }
        }

        private void getPayParamList()
        {
            payJson = HttpUtils.post("http://192.168.1.169/morechannel/index.php/MoreChannel/getproductinfo", null);
            DataContractJsonSerializer js = new DataContractJsonSerializer(typeof(PayParam));
            using (MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(payJson)))
            {
                PayParam list = js.ReadObject(ms) as PayParam;
                if (list.result.code == 200)
                {
                    PayParam.Result.Payinfo[] payInfo = list.result.payinfo;
                    for (int i = 0; i < payInfo.Length; i++)
                    {
                        Label labelPayId = new Label();
                        labelPayId.AutoSize = true;
                        labelPayId.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                        labelPayId.Location = new System.Drawing.Point(10, 0 + 150 * i);
                        labelPayId.Text = "pay_id：";
                        panel2.Controls.Add(labelPayId);

                        Label labelPayMoney = new Label();
                        labelPayMoney.AutoSize = true;
                        labelPayMoney.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                        labelPayMoney.Location = new System.Drawing.Point(10, 30 + 150 * i);
                        labelPayMoney.Text = "pay_money：";
                        panel2.Controls.Add(labelPayMoney);

                        Label labelProductId = new Label();
                        labelProductId.AutoSize = true;
                        labelProductId.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                        labelProductId.Location = new System.Drawing.Point(10, 60 + 150 * i);
                        labelProductId.Text = "product_id：";
                        panel2.Controls.Add(labelProductId);

                        Label labelProductName = new Label();
                        labelProductName.AutoSize = true;
                        labelProductName.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                        labelProductName.Location = new System.Drawing.Point(10, 90 + 150 * i);
                        labelProductName.Text = "product_name：";
                        panel2.Controls.Add(labelProductName);

                        TextBox textPayId = new TextBox();
                        textPayId.Size = new System.Drawing.Size(290, 20);
                        textPayId.Name = "pay_id";
                        textPayId.ReadOnly = true;
                        textPayId.Location = new System.Drawing.Point(130, 0 + 150 * i);
                        textPayId.Text = payInfo[i].id;
                        panel2.Controls.Add(textPayId);

                        TextBox textPayMoney = new TextBox();
                        textPayMoney.Size = new System.Drawing.Size(290, 20);
                        textPayMoney.Name = "pay_money";
                        textPayMoney.ReadOnly = true;
                        textPayMoney.Location = new System.Drawing.Point(130, 30 + 150 * i);
                        textPayMoney.Text = payInfo[i].money;
                        panel2.Controls.Add(textPayMoney);

                        TextBox textProductId = new TextBox();
                        textProductId.Size = new System.Drawing.Size(290, 20);
                        textProductId.Name = "product_id";
                        textProductId.ReadOnly = true;
                        textProductId.Location = new System.Drawing.Point(130, 60 + 150 * i);
                        textProductId.Text = payInfo[i].product_id;
                        panel2.Controls.Add(textProductId);

                        TextBox textProductName = new TextBox();
                        textProductName.Size = new System.Drawing.Size(290, 20);
                        textProductName.Name = "product_name";
                        textProductName.ReadOnly = true;
                        textProductName.Location = new System.Drawing.Point(130, 90 + 150 * i);
                        textProductName.Text = payInfo[i].product_name;
                        panel2.Controls.Add(textProductName);

                        Label line = new Label();
                        line.AutoSize = true;
                        line.Font = new System.Drawing.Font("宋体", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
                        line.Location = new System.Drawing.Point(10, 120 + 150 * i);
                        line.Text = "--------------------------------------------";
                        panel2.Controls.Add(line);
                    }
                }
            }
        }
    }
}
