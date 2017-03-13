using System;
using System.Windows.Forms;
using System.Diagnostics;
using System.Collections;
using System.Xml;
using System.Threading;
using System.IO;
using System.Text;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

namespace TianyouMultiChannel
{
    public partial class PackingToolsForm : Form
    {
        private String apkPath;
        private String apkName;

        //private Thread thread;
        private int time = 0;   //打包计时

        private Hashtable mAllChannelTable = new Hashtable();        //所有渠道列表
        private Hashtable mChooseChannelTable = new Hashtable();     //选中渠道列表

        public PackingToolsForm()
        {
            InitializeComponent();
            getMainChannelList();
        }

        private void PackingToolsForm_Load(object sender, EventArgs e)
        {
            CheckForIllegalCrossThreadCalls = false;
        }

        private void getMainChannelList()
        {
            Hashtable param = new Hashtable();
            param.Add("token", "tianyouhudong");
            String json = HttpUtils.post("http://api.tianyouxi.com/index.php?c=channel&a=GetChannels", param);
            System.Console.WriteLine("Http请求：" + json);

            DataContractJsonSerializer js = new DataContractJsonSerializer(typeof(ChannelInfo));
            using (MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(json)))
            {
                ChannelInfo info = js.ReadObject(ms) as ChannelInfo;
                if (info.result.code == 200)
                {
                    listBox1.Items.Clear();
                    ChannelInfo.Result.Productinfo[] productInfo = info.result.productinfo;
                    for (int i = 0; i < productInfo.Length; i++)
                    {
                        TreeNode node = new TreeNode();
                        node.Text = productInfo[i].name + ":" + productInfo[i].channelid;
                        treeView1.Nodes.Add(node);
                        Hashtable param2 = new Hashtable();
                        param2.Add("token", "tianyouhudong");
                        param2.Add("channelid", productInfo[i].id);
                        String html = HttpUtils.post("http://api.tianyouxi.com/index.php?c=channel&a=GetChannelchilds", param2);
                        parseChildChannel(html, node);
                    }
                }
            }
        }

        private void parseChildChannel(String json, TreeNode node)
        {
            mAllChannelTable.Clear();
            DataContractJsonSerializer js = new DataContractJsonSerializer(typeof(ChannelInfo));
            using (MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(json)))
            {
                ChannelInfo info = js.ReadObject(ms) as ChannelInfo;
                if (info.result.code == 200)
                {
                    listBox1.Items.Clear();
                    ChannelInfo.Result.Productinfo[] productInfo = info.result.productinfo;
                    for (int i = 0; i < productInfo.Length; i++)
                    {
                        mAllChannelTable.Add(productInfo[i].name, productInfo[i].channelid);
                        TreeNode cNode = new TreeNode();
                        cNode.Text = productInfo[i].name + ":" + productInfo[i].channelid;
                        node.Nodes.Add(cNode);
                    }
                }
            }
        }

        private void textBox1_DragEnter(object sender, DragEventArgs e)
        {
            string path = ((System.Array)e.Data.GetData(DataFormats.FileDrop)).GetValue(0).ToString();
            textBox1.Text = path;
            apkPath = path;
            String safeName = path.Substring(path.LastIndexOf("\\") + 1);
            apkName = safeName.Substring(0, safeName.LastIndexOf("."));
        }

        //点击打包按钮
        private void button2_Click(object sender, EventArgs e)
        {
            if (textBox1.Text.Length == 0)
            {
                MessageBox.Show("请先上传游戏主包！", "提示", MessageBoxButtons.OK);
                return;
            }
            if (mChooseChannelTable.Count == 0)
            {
                MessageBox.Show("请先选择渠道！", "提示", MessageBoxButtons.OK);
                return;
            }
            timer1.Enabled = true;
            time = 0;
            packingBtn.Enabled = false;
            if (!Directory.Exists("package"))
            {
                Directory.CreateDirectory("package");
            }
            new Thread(packing).Start();
            //thread = new Thread(new ThreadStart(packing));
            //thread.Start();
        }

        //开始打包
        private void packing()
        {
            int progress = 0;
            progressBar1.Value = 0;
            label5.Text = progress++ + " / " + mChooseChannelTable.Count;
            ApkUtils apkUtils = new ApkUtils(logListBox);
            //1. 解包
            apkUtils.unApk(apkPath);
            foreach (String channelName in mChooseChannelTable.Keys)
            {
                progressBar1.Value = 0;
                progressBar1.Value++;
                String channelId = (String)mChooseChannelTable[channelName];
                label5.Text = progress++ + " / " + mChooseChannelTable.Count;
                //2. 修改包名
                if (textBox2.Text.Length != 0)
                {
                    //logListBox.AppendText("修改包名：" + "\r\n");
                    modifiPackageName();
                }
                progressBar1.Value++;
                //3. 修改渠道号
                //logListBox.AppendText("修改渠道号：" + "\r\n");
                modifiChannelId(channelId);
                progressBar1.Value++;
                //4. 合包
                apkUtils.joinApk(apkName);
                progressBar1.Value++;
                //5. 签名
                apkUtils.signApk(apkName);
                progressBar1.Value++;
                //6. 优化
                apkUtils.alignApk(apkName, "package" + "\\" + apkName + "_" + channelId);
                progressBar1.Value++;
            }
            //7. 删除临时文件
            apkUtils.deleteFile(apkName);
            timer1.Enabled = false;
            MessageBox.Show("打包完成，打包时间：" + label6.Text, "提示", MessageBoxButtons.OK);
            logListBox.AppendText("打包完成");
            packingBtn.Enabled = true;
        }

        //修改包名
        private void modifiPackageName()
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(apkName + "\\AndroidManifest.xml");
            //try
            //{
            //    xmlDoc.Load(apkName + "\\AndroidManifest.xml");
            //}
            //catch
            //{
            //    Thread.Sleep(1000);
            //    modifiPackageName();
            //    return;
            //}
            XmlNode manifest = xmlDoc.SelectSingleNode("manifest");
            manifest.Attributes["package"].Value = textBox2.Text;
            xmlDoc.Save(apkName + "\\AndroidManifest.xml");
        }

        private void modifiChannelId(String channelId)
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(apkName + "\\assets\\tianyou_config.xml");
            //try
            //{
            //    xmlDoc.Load(apkName + "\\assets\\TianGame.xml");
            //}
            //catch
            //{
            //    Thread.Sleep(1000);
            //    modifiChannelId(channelId);
            //    return;
            //}
            XmlNode infos = xmlDoc.SelectSingleNode("infos");
            XmlNode channel = infos.SelectSingleNode("channel_id");
            channel.InnerText = channelId;
            xmlDoc.Save(apkName + "\\assets\\tianyou_config.xml");
        }

        private void 开始打包ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            System.Diagnostics.Process.Start("explorer.exe", "package");
        }

        private void button1_Click(object sender, EventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog();
            if (dialog.ShowDialog() == DialogResult.OK)
            {
                textBox1.Text = dialog.FileName;
                apkPath = dialog.FileName;
                String safeName = dialog.SafeFileName;
                apkName = safeName.Substring(0, safeName.LastIndexOf("."));
            }
        }

        private void 后台渠道列表ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            treeView1.Nodes.Clear();
            getMainChannelList();
        }

        private void 本地渠道列表ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            listBox1.Items.Clear();
            readParseXml();
        }

        //读取渠道信息Xml
        private void readParseXml()
        {
            XmlDocument xmlDoc = new XmlDocument();
            try
            {
                xmlDoc.Load("channel\\channel_info.xml");
            }
            catch (Exception)
            {
                MessageBox.Show("没有配置本地渠道信息", "提示", MessageBoxButtons.OK);
                return;
            }
            XmlNode channel_list = xmlDoc.SelectSingleNode("channel_list");
            XmlNodeList channel = channel_list.SelectNodes("channel");
        }

        private void treeView1_NodeMouseDoubleClick(object sender, TreeNodeMouseClickEventArgs e)
        {
            String channelName = treeView1.SelectedNode.Text;
            String[] str = channelName.Split('-');
            try
            {
                mChooseChannelTable.Add(channelName.Split(':')[0], channelName.Split(':')[1]);
            }
            catch (Exception)
            {
                if (e.Node.Parent != null)
                    MessageBox.Show("该渠道已添加", "提示", MessageBoxButtons.OK);
                return;
            }
            listBox1.Items.Add(channelName);
        }

        private void button4_Click(object sender, EventArgs e)
        {
            if (listBox1.SelectedIndex != -1)
            {
                String channelInfo = (String)listBox1.SelectedItem;
                mChooseChannelTable.Remove(channelInfo.Substring(0, channelInfo.IndexOf(":")));
                listBox1.Items.RemoveAt(listBox1.SelectedIndex);
            }
        }

        private void button5_Click(object sender, EventArgs e)
        {
            listBox1.Items.Clear();
        }

        private void button6_Click(object sender, EventArgs e)
        {
            logListBox.Clear();
        }

        private void showLog(String log){
            logListBox.AppendText(log + "\r\n");
        }

        private void PackingToolsForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            //if (thread != null)
            //    thread.Abort();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            time++;
            label6.Text = time / 60 + " : " + time % 60;
        }
    }
}
