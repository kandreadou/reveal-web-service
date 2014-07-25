package gr.iti.mklab.reveal.visual;

import eu.socialsensor.framework.client.dao.impl.MediaItemDAOImpl;
import eu.socialsensor.framework.client.search.visual.VisualIndexHandler;
import gr.iti.mklab.visual.aggregation.VladAggregatorMultipleVocabularies;
import gr.iti.mklab.visual.datastructures.AbstractSearchStructure;
import gr.iti.mklab.visual.datastructures.IVFPQ;
import gr.iti.mklab.visual.datastructures.Linear;
import gr.iti.mklab.visual.datastructures.PQ;
import gr.iti.mklab.visual.dimreduction.PCA;
import gr.iti.mklab.visual.extraction.AbstractFeatureExtractor;
import gr.iti.mklab.visual.extraction.SURFExtractor;
import gr.iti.mklab.visual.vectorization.ImageVectorization;

/**
 * Created by kandreadou on 7/21/14.
 */
public class IndexingManager {

    protected static int maxNumPixels = 768 * 512;
    protected static int targetLengthMax = 1024;
    protected static AbstractSearchStructure index;

    public static void createIndex(String name) throws Exception {
        String learningFolder = "/home/kandreadou/webservice/learning_files/";


        int[] numCentroids = {128, 128, 128, 128};
        int initialLength = numCentroids.length * numCentroids[0] * AbstractFeatureExtractor.SURFLength;

        String[] codebookFiles = {
                learningFolder + "surf_l2_128c_0.csv",
                learningFolder + "surf_l2_128c_1.csv",
                learningFolder + "surf_l2_128c_2.csv",
                learningFolder + "surf_l2_128c_3.csv"
        };

        String pcaFile = learningFolder + "pca_surf_4x128_32768to1024.txt";


        int maximumNumVectors = 1000;
        int m2 = 64;
        int k_c = 256;
        int numCoarseCentroids = 8192;
        String coarseQuantizerFile2 = learningFolder + "qcoarse_1024d_8192k.csv";
        String productQuantizerFile2 = learningFolder + "pq_1024_64x8_rp_ivf_8192k.csv";
        String ivfpqIndexFolder = learningFolder + name + "_" + targetLengthMax;
        IVFPQ ivfpq_1 = new IVFPQ(targetLengthMax, maximumNumVectors, false, ivfpqIndexFolder, m2, k_c, PQ.TransformationType.RandomPermutation, numCoarseCentroids, true, 0);
        ivfpq_1.loadCoarseQuantizer(coarseQuantizerFile2);
        ivfpq_1.loadProductQuantizer(productQuantizerFile2);
        int w = 64; // larger values will improve results/increase seach time
        ivfpq_1.setW(w); // how many (out of 8192) lists should be visited during search.
        //visualIndex = new Linear(targetLengthMax, 10000000, false, BDBEnvHome, false, false, 0);
        //int existingVectors = visualIndex.getLoadCounter();
        SURFExtractor extractor = new SURFExtractor();
        ImageVectorization.setFeatureExtractor(extractor);
        ImageVectorization.setVladAggregator(new VladAggregatorMultipleVocabularies(codebookFiles,
                numCentroids, AbstractFeatureExtractor.SURFLength));
        if (targetLengthMax < initialLength) {
            System.out.println("targetLengthMax : " + targetLengthMax + " initialLengh " + initialLength);
            PCA pca = new PCA(targetLengthMax, 1, initialLength, true);
            pca.loadPCAFromFile(pcaFile);
            ImageVectorization.setPcaProjector(pca);
        }
        //vectorizer = new ImageVectorizer("surf", "no", codebookFiles,
        //numCentroids, targetLengthMax, pcaFile, 50);
    }
}
