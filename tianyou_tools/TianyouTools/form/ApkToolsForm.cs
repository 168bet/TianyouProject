using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Collections;
using System.Diagnostics;
using System.IO;

namespace TianyouMultiChannel
{
    public partial class ApkToolsForm : Form
    {
        private String apkName;
        private ApkUtils apkUtils;

        public ApkToolsForm()
        {
            InitializeComponent();
            apkUtils = new ApkUtils(textBox3);
        }

        //导入主包
        private void button1_Click(object sender, EventArgs e)
        {
            if (radioButton2.Checked)
            {
                FolderBrowserDialog fbd = new FolderBrowserDialog();
                if (fbd.ShowDialog() == DialogResult.OK)
                {
                    textBox1.Text = fbd.SelectedPath;
                }
            }
            else
            {
                OpenFileDialog dialog = new OpenFileDialog();
                if (dialog.ShowDialog() == DialogResult.OK)
                {
                    textBox1.Text = dialog.FileName;
                }
            }
        }

        //反编译操作
        private void button3_Click(object sender, EventArgs e)
        {
            if (radioButton1.Checked) //反编译APK
            {
                String safeName = textBox1.Text.Substring(textBox1.Text.LastIndexOf("\\") + 1);
                apkName = safeName.Substring(0, safeName.LastIndexOf("."));
                apkUtils.unApk(textBox1.Text);
                apkUtils.moveFile(apkName, textBox1.Text.Substring(0, textBox1.Text.IndexOf('.')));
            }
            else if (radioButton2.Checked) //重打包APK
            {
                apkUtils.joinApk(textBox1.Text);
                apkUtils.moveFile(apkName, textBox1.Text);
            }
            else if (radioButton5.Checked) //jar2dex
            {
                //apkUtils.jar2dex();
                apkUtils.moveFile(apkName, textBox1.Text);
            }
        }

        private void textBox1_DragEnter(object sender, DragEventArgs e)
        {
            string path = ((System.Array)e.Data.GetData(DataFormats.FileDrop)).GetValue(0).ToString();
            textBox1.Text = path;
        }
    }
}
