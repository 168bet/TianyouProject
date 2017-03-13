using System;
using System.Diagnostics;
using System.Windows.Forms;

namespace TianyouMultiChannel
{
    class ApkUtils
    {
        private TextBox mTextBox;

        public ApkUtils(TextBox textBox)
        {
            mTextBox = textBox;
        }

        //解包
        public void unApk(String apkPath)
        {
            String cmd = "tools\\" + "apktool.bat d " + apkPath;
            executeCmd(cmd);
        }

        //合包
        public void joinApk(String apkName)
        {
            String cmd = "tools\\apktool.bat b " + apkName;
            executeCmd(cmd);
        }

        //签名
        public void signApk(String apkName)
        {
            String cmd = "jarsigner -digestalg SHA1 -sigalg MD5withRSA -keystore tools\\tianyouxi.keystore " +
                "-storepass tianyouxi -keypass tianyouxi " + apkName + "\\dist\\" + apkName + ".apk tianyouxi";
            executeCmd(cmd);
        }

        //优化
        public void alignApk(String apkName, String alignName)
        {
            String cmd = "tools\\zipalign -f -v 4 " + apkName + "\\dist\\" + apkName + ".apk " + alignName + ".apk";
            executeCmd(cmd);
        }

        //删除文件
        public void deleteFile(String fileName)
        {
            String cmd = "rd/s/q " + fileName;
            executeCmd(cmd);
        }

        //移动文件
        public void moveFile(String sourceFile, String aimFile)
        {
            String cmd = "move " + sourceFile + " " + aimFile;
            executeCmd(cmd);
        }

        public void jar2dex(String jarName)
        {
            String cmd = "java -jar tools\\dx.jar --dex --output " + "temp\\classes.dex " + jarName;
            executeCmd(cmd);
        }

        public void dex2smali()
        {
            String cmd = "java -jar tools\\baksmali.jar " + "temp\\classes.dex";
            executeCmd(cmd);
        }

        //复制文件
        public void copyFile(String srcFile, String aimFile)
        {
            String cmd = "xcopy /E/I/D/Y " + srcFile + " " + aimFile;
            executeCmd(cmd);
        }

        private void executeCmd(String cmd)
        {
            showLog("cmd:" + cmd);
            Process proc = new Process();
            proc.StartInfo.CreateNoWindow = true;
            proc.StartInfo.FileName = "cmd.exe";
            proc.StartInfo.UseShellExecute = false;
            //proc.StartInfo.RedirectStandardError = true;
            proc.StartInfo.RedirectStandardInput = true;
            proc.StartInfo.RedirectStandardOutput = true;
            proc.Start();
            proc.StandardInput.WriteLine(cmd);
            proc.StandardInput.WriteLine("exit");
            while (!proc.StandardOutput.EndOfStream)
            {
                showLog(proc.StandardOutput.ReadLine());
                System.Console.WriteLine(proc.StandardOutput.ReadLine());
            }
        }

        delegate void SetTextCallback(string text);

        private void showLog(String log)
        {
            if (this.mTextBox.InvokeRequired)
            {
                while (!this.mTextBox.IsHandleCreated)
                {
                    //解决窗体关闭时出现“访问已释放句柄“的异常
                    if (this.mTextBox.Disposing || this.mTextBox.IsDisposed)
                        return;
                }
                SetTextCallback d = new SetTextCallback(showLog);
                this.mTextBox.Invoke(d, new object[] { log });
            }
            else
            {
                mTextBox.AppendText(log + "\r\n");
            }
        }
    }
}
