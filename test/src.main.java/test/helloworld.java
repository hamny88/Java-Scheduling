package test;
import java.io.StringWriter;
import org.apache.velocity.app.*;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public class helloworld {
	public static void main(String[] args)throws Exception
	{
		//first get and initilaize an engine
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		
		VelocityContext context = new VelocityContext();
		context.put("name", "World");
		//next get the Template
		Template t = ve.getTemplate("helloworld.vm");

	//create context and data
	
     /* now render the template into a StringWriter */
     StringWriter writer = new StringWriter();
     t.merge( context, writer );
     /* show the World */
     System.out.println( writer.toString() );   
	}
}
