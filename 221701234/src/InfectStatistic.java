import java.io.File;
import java.lang.String;
import java.util.ArrayList;
import java.util.LinkedList;
class InfectStatistic {
    public static void main(String[] args) 
    {
        //用来保存路径以及日期
        String log_Path,out_Path,date;
        log_Path="";
        out_Path="";
        date="";
        ArrayList<String> type,province;
        type=new ArrayList<String>();
        province=new ArrayList<String>();
        if(args.length==0||!args[0].equals("list"))
        {
            System.out.println("您输入的命令有误，请检查后重新输入");
        }
        else
        {
            //用来从命令中获取所需参数
            for(int i=1;i<args.length;++i)
            {
                if(args[i].equals("-log"))
                {
                    log_Path=args[++i];
                }
                else if(args[i].equals("-out"))
                {
                    out_Path=args[++i];
                }
                else if(args[i].equals("-date"))
                {
                    date=args[++i];
                }
                else if(args[i].equals("-type"))
                {
                    for(++i;i<args.length;++i)
                    {
                        if(!args[i].equals("-province"))
                        {
                        type.add(args[i]);
                        }
                        else 
                        {
                            --i;
                            break;
                        }
                    }
                }
                else if(args[i].equals("-province"))
                {
                    for(++i;i<args.length;++i)
                    {
                        if(!args[i].equals("-type"))
                        {
                        province.add(args[i]);
                        }
                        else 
                        {
                            --i;
                            break;
                        }
                    } 
                }
            }
            
            
            //实现从给定的日志路径中筛选出需要进行统计的文件路径
            File file=new File(log_Path);
            LinkedList<File> filelist=new LinkedList<File>();
            if(file.exists())//路径存在
            {
                //用户未指定日期
                if(date.length()==0)
                {
                    for(File file2:file.listFiles())
                    {
                        filelist.add(file2);
                    }
                }
                else
                {
                    String templog=log_Path+"\\"+date+".log.txt";
                    for(File file2:file.listFiles())
                    {
                        if(file2.getAbsolutePath().compareTo(templog)<0)
                        {
                            filelist.add(file2);
                        }
                    }
                }
                System.out.println(filelist);
            }
            else
            {
                System.out.println("您输入的日志文件路径有误");
                return;
            }
        }
    }
}
