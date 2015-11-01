package fr.lis.ikeyplus.rest;

import fr.lis.ikeyplus.io.SDDSaxParser;
import fr.lis.ikeyplus.io.SingleAccessKeyTreeDumper;
import fr.lis.ikeyplus.model.SingleAccessKeyTree;
import fr.lis.ikeyplus.services.IdentificationKeyGenerator;
import fr.lis.ikeyplus.utils.IkeyConfig;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Identification key webservice using REST protocol
 *
 * @author Florian Causse
 * @created 06-04-2011
 */
@Path("/identificationKey")
public class IdentificationKeyImpl {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String createIdentificationKey(
            @FormParam("sddURL") String sddURL,
            @FormParam("format") String format,
            @FormParam("representation") String representation,
            @FormParam("fewStatesCharacterFirst") String fewStatesCharacterFirst,
            @FormParam("mergeCharacterStatesIfSameDiscrimination") String mergeCharacterStatesIfSameDiscrimination,
            @FormParam("pruning") String pruning, @FormParam("verbosity") String verbosity,
            @FormParam("scoreMethod") String scoreMethod, @FormParam("weightContext") String weightContext,
            @FormParam("weightType") String weightType) {

        // creation of IkeyConfig object (containing options)
        IkeyConfig config = new IkeyConfig();
        // String containing the name of the result file
        String resultFileName = null;
        // String containing the URL of the result file
        String resultFileUrl = null;
        String lineReturn = System.getProperty("line.separator");

        try {

            // define header string
            StringBuffer header = new StringBuffer();

            // options initialization
            if (format != null && IkeyConfig.OutputFormat.fromString(format) != null) {
                config.setFormat(IkeyConfig.OutputFormat.fromString(format));
            } else {
                config.setFormat(IkeyConfig.OutputFormat.TXT);
            }

            if (representation != null && IkeyConfig.KeyRepresentation.fromString(representation) != null) {
                config.setRepresentation(IkeyConfig.KeyRepresentation.fromString(representation));
            } else {
                config.setRepresentation(IkeyConfig.KeyRepresentation.FLAT);
            }
            if (fewStatesCharacterFirst != null && fewStatesCharacterFirst.equalsIgnoreCase(IkeyConfig.YES)) {
                config.setFewStatesCharacterFirst(true);
            }
            if (mergeCharacterStatesIfSameDiscrimination != null
                    && mergeCharacterStatesIfSameDiscrimination.equalsIgnoreCase(IkeyConfig.YES)) {
                config.setMergeCharacterStatesIfSameDiscrimination(true);
            }
            if (pruning != null && pruning.equalsIgnoreCase(IkeyConfig.YES)) {
                config.setPruning(true);
            }
            if (verbosity != null && IkeyConfig.VerbosityLevel.fromString(verbosity) != null) {
                config.setVerbosity(IkeyConfig.VerbosityLevel.fromString(verbosity));
            }
            if (scoreMethod != null && IkeyConfig.ScoreMethod.fromString(scoreMethod) != null) {
                config.setScoreMethod(IkeyConfig.ScoreMethod.fromString(scoreMethod));
            }
            if (weightContext != null && IkeyConfig.WeightContext.fromString(weightContext) != null) {
                config.setWeightContext(IkeyConfig.WeightContext.fromString(weightContext));
            }
            if (weightType != null && weightType.equalsIgnoreCase(IkeyConfig.WeightType.CONTEXTUAL.toString())) {
                config.setWeightType(IkeyConfig.WeightType.CONTEXTUAL);
            }

//			// calculate CPU usage
//			double usageCPU = 0;
//			try {
//				usageCPU = new Sigar().getCpuPerc().getCombined();
//			} catch (SigarException e) {
//				e.printStackTrace();
//				config.setErrorMessage(IkeyConfig.getBundleConfElement("message.cpuUsageError"), e);
//			}

            // if CPU usage is less than 80%
//			if (usageCPU < 0.8) {

            long beforeTime = System.currentTimeMillis();

            // call SDD parser
            SDDSaxParser sddSaxParser = null;
            try {
                // test if the URL is valid
                URLConnection urlConnection;
                InputStream httpStream;
                try {
                    URL fileURL = new URL(sddURL);
                    // open URL (HTTP query)
                    urlConnection = fileURL.openConnection();
                    // Open data stream
                    httpStream = urlConnection.getInputStream();
                } catch (java.net.MalformedURLException e) {
                    e.printStackTrace();
                    config.setErrorMessage(IkeyConfig.getBundleConfElement("message.urlError"), e);
                } catch (IOException e) {
                    e.printStackTrace();
                    config.setErrorMessage(IkeyConfig.getBundleConfElement("message.urlError"), e);
                }
                sddSaxParser = new SDDSaxParser(sddURL, config);
                // construct header
                header.append(lineReturn + sddSaxParser.getDataset().getLabel() + ", "
                        + IkeyConfig.getBundleConfOverridableElement("message.createdBy") + lineReturn);
                header.append(lineReturn + "Options:");
                header.append(lineReturn + "sddURL=" + sddURL);
                header.append(lineReturn + "format=" + config.getFormat());
                header.append(lineReturn + "representation=" + config.getRepresentation());
                header.append(lineReturn + "fewStatesCharacterFirst=" + config.isFewStatesCharacterFirst());
                header.append(lineReturn + "mergeCharacterStatesIfSameDiscrimination="
                        + config.isMergeCharacterStatesIfSameDiscrimination());
                header.append(lineReturn + "pruning=" + config.isPruning());
                header.append(lineReturn + "verbosity=" + config.getVerbosity());
                header.append(lineReturn + "scoreMethod=" + config.getScoreMethod());
                header.append(lineReturn + "weightContext=" + config.getWeightContext());
                header.append(lineReturn + "weightType=" + config.getWeightType());
                header.append(lineReturn);
            } catch (Throwable t) {
                t.printStackTrace();
                config.setErrorMessage(IkeyConfig.getBundleConfElement("message.parsingError"), t);
            }
            double parseDuration = (double) (System.currentTimeMillis() - beforeTime) / 1000;
            beforeTime = System.currentTimeMillis();

            // call identification key service
            IdentificationKeyGenerator identificationKeyGenerator = null;
            try {
                identificationKeyGenerator = new IdentificationKeyGenerator(sddSaxParser.getDataset(),
                        config);
                identificationKeyGenerator.createIdentificationKey();
            } catch (Throwable t) {
                t.printStackTrace();
                config.setErrorMessage(IkeyConfig.getBundleConfElement("message.creatingKeyError"), t);
            }

            double keyCreationDuration = (double) (System.currentTimeMillis() - beforeTime) / 1000;
            // construct header
            header.append(System.getProperty("line.separator") + "parseDuration= " + parseDuration + "s");
            header.append(System.getProperty("line.separator") + "keyCreationDuration= "
                    + keyCreationDuration + "s");

            File resultFile = null;

            if (identificationKeyGenerator != null
                    && identificationKeyGenerator.getSingleAccessKeyTree() != null) {

                try {
                    // creation of the directory containing key files
                    if (!new File(IkeyConfig.getBundleConfOverridableElement("generatedKeyFiles.prefix")
                            + IkeyConfig.getBundleConfOverridableElement("generatedKeyFiles.folder")).exists()) {
                        new File(IkeyConfig.getBundleConfOverridableElement("generatedKeyFiles.prefix")
                                + IkeyConfig.getBundleConfOverridableElement("generatedKeyFiles.folder"))
                                .mkdir();
                    }

                    SingleAccessKeyTree tree2dump = identificationKeyGenerator.getSingleAccessKeyTree();

                    header.append(System.getProperty("line.separator")
                            + System.getProperty("line.separator"));

                    if (!config.getVerbosity().contains(IkeyConfig.VerbosityLevel.HEADER)) {
                        header.setLength(0);
                    }
//						if (config.getFormat().equalsIgnoreCase(IkeyConfig.PDF)) {
//							if (config.getRepresentation().equalsIgnoreCase(IkeyConfig.FLAT)) {
//								resultFile = SingleAccessKeyTreeDumper.dumpFlatPdfFile(header.toString(),
//										tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
//							} else {
//								resultFile = SingleAccessKeyTreeDumper.dumpPdfFile(header.toString(),
//										tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
//							}
//						}
                    else if (config.getFormat() == IkeyConfig.OutputFormat.HTML) {
                        if (config.getRepresentation() == IkeyConfig.KeyRepresentation.FLAT) {
                            resultFile = SingleAccessKeyTreeDumper.dumpFlatHtmlFile(header.toString(),
                                    tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                        } else {
                            resultFile = SingleAccessKeyTreeDumper.dumpHtmlFile(header.toString(),
                                    tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                        }
                    } else if (config.getFormat() == IkeyConfig.OutputFormat.WIKI) {
                        if (config.getRepresentation() == IkeyConfig.KeyRepresentation.FLAT) {
                            resultFile = SingleAccessKeyTreeDumper.dumpFlatWikiFile(header.toString(),
                                    tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                        } else {
                            resultFile = SingleAccessKeyTreeDumper.dumpWikiFile(header.toString(),
                                    tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                        }
                    } else if (config.getFormat() == IkeyConfig.OutputFormat.SPECIES_ID_WIKI_STATEMENT) {
                        resultFile = SingleAccessKeyTreeDumper.dumpFlatSpeciesIDStatementWikiFile(
                                header.toString(), tree2dump);
                    } else if (config.getFormat() == IkeyConfig.OutputFormat.INTERACTIVE_HTML) {
                        resultFile = SingleAccessKeyTreeDumper.dumpInteractiveHtmlFile(header.toString(),
                                tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                    } else if (config.getFormat() == IkeyConfig.OutputFormat.SPECIES_ID_WIKI_QUESTION_ANSWER) {
                        resultFile = SingleAccessKeyTreeDumper.dumpFlatSpeciesIDQuestionAnswerWikiFile(
                                header.toString(), tree2dump);
                    } else if (config.getFormat() == IkeyConfig.OutputFormat.DOT) {
                        resultFile = SingleAccessKeyTreeDumper.dumpDotFile(header.toString(), tree2dump);
                    } else if (config.getFormat() == IkeyConfig.OutputFormat.SDD) {
                        resultFile = SingleAccessKeyTreeDumper.dumpSddFile(header.toString(), tree2dump);
                    } else if (config.getFormat() == IkeyConfig.OutputFormat.ZIP) {
                        resultFile = SingleAccessKeyTreeDumper.dumpZipFile(header.toString(), tree2dump,
                                config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                    } else {
                        if (config.getRepresentation() == IkeyConfig.KeyRepresentation.FLAT) {
                            resultFile = SingleAccessKeyTreeDumper.dumpFlatTxtFile(header.toString(),
                                    tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                        } else {
                            resultFile = SingleAccessKeyTreeDumper.dumpTxtFile(header.toString(),
                                    tree2dump, config.getVerbosity().contains(IkeyConfig.VerbosityLevel.STATISTIC));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    config.setErrorMessage(IkeyConfig.getBundleConfElement("message.creatingFileError"));
                }
                // initiate the result file name
                resultFileName = resultFile.getName();

            } else {
                config.setErrorMessage(IkeyConfig.getBundleConfElement("message.creatingKeyError"));
            }

            // if CPU usage is more than 80%
//			} else {
//				config.setErrorMessage(IkeyConfig.getBundleConfElement("message.serverBusy"));

//			}

        } catch (Exception e) {
            e.printStackTrace();
            config.setErrorMessage(IkeyConfig.getBundleConfElement("message.error"), e);
        }

        // initialize the file name with error file name if exist
        if (config.getErrorMessageFile() != null) {
            resultFileName = config.getErrorMessageFile().getName();
        }

        resultFileUrl = IkeyConfig.getBundleConfOverridableElement("host")
                + IkeyConfig.getBundleConfOverridableElement("generatedKeyFiles.folder") + resultFileName;

        return resultFileUrl;
    }
}