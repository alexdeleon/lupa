import com.lumata.lib.webscraper.extractor.internal.HtmlExtractor;
import com.lumata.lib.webscraper.extractor.internal.ImageExtractor;
import com.lumata.lib.webscraper.extractor.internal.config.AbstractExtractorConfigurationModule;

/*
* 2011 copyright Buongiorno SpA
*/


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
   }
}
