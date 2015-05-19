package net.minecraftforge.gradle.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraftforge.gradle.common.Constants;
import net.minecraftforge.gradle.util.caching.Cached;
import net.minecraftforge.gradle.util.caching.CachedTask;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public class Download extends CachedTask
{
    @Input
    private Object url;

    @OutputFile
    @Cached
    private Object output;

    @TaskAction
    public void doTask() throws IOException
    {
        File outputFile = getProject().file(getOutput());
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        getLogger().info("Downloading " + getUrl() + " to " + outputFile);

        HttpURLConnection connect = (HttpURLConnection) (new URL(getUrl())).openConnection();
        connect.setRequestProperty("User-Agent", Constants.USER_AGENT);
        connect.setInstanceFollowRedirects(true);

        InputStream inStream = connect.getInputStream();
        OutputStream outStream = new FileOutputStream(outputFile);

        int data = inStream.read();
        while (data != -1)
        {
            outStream.write(data);

            // read next
            data = inStream.read();
        }

        inStream.close();
        outStream.flush();
        outStream.close();

        getLogger().info("Download complete");
    }

    public File getOutput()
    {
        return getProject().file(output);
    }

    public void setOutput(Object output)
    {
        this.output = output;
    }

    public String getUrl()
    {
        return Constants.resolveString(url);
    }

    public void setUrl(Object url)
    {
        this.url = url;
    }
}
