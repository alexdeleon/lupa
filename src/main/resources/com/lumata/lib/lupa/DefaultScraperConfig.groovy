import com.lumata.lib.lupa.extractor.internal.HtmlExtractor;
import com.lumata.lib.lupa.extractor.internal.ImageExtractor;
import com.lumata.lib.lupa.extractor.internal.YouTubeExtractor;
import com.lumata.lib.lupa.extractor.internal.config.AbstractExtractorConfigurationModule;

/**
* @author Alexander De Leon - alexander.leon@buongiorno.com
*
*/
class DefaultScraperConfig extends AbstractExtractorConfigurationModule {

   void configure(){
	   when([type:'text/html'])
			   .use(HtmlExtractor.class)
	   when([type:'image/*'])
			   .use(ImageExtractor.class)
   	   when([url:'(?:https?://(?:(?:youtu\\.be/|(?:(?:www.)?youtube.com(?:/v/|/embed/|/watch\\?.*v=))))([\\w-_]{11})(?:[^\\w-_]+.*)?)|(?:https?://(?:www.)?youtube.com/user/([^\\?]*)(?:/|\\?.*)?)'])
		  		.use(YouTubeExtractor.class).withParameters(['youtube.developerKey':'AI39si6H1DiUOl6zP7mhpRsTbrxVzT8VqD2I-G72gcLo5AphVUk2VYGlir49_yhisXW05xRJ9WOLvRNhQxaLfst70YwcT2I5DQ'])
   }
}
