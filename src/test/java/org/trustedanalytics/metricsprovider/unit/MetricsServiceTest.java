/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.metricsprovider.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.trustedanalytics.metricsprovider.rest.MetricsDownloadTasks;
import org.trustedanalytics.metricsprovider.rest.MetricsService;
import org.trustedanalytics.metricsprovider.unit.resources.DownloadTasksStatus;
import org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources;
import org.trustedanalytics.metricsprovider.unit.resources.TestDownloadTasks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class MetricsServiceTest {

    private MetricsService sut;

    private UUID org = UUID.fromString("61d09abc-4ee3-4381-9d60-829d2e30d52e");
    private UUID space1 = UUID.fromString("91a5b5f8-a9d2-4a80-b05a-ddef55b31a7a");
    private UUID space2 = UUID.fromString("4ff3f093-abc4-492b-9993-99ec051607ab");

    @Mock
    private MetricsDownloadTasks metricsDownloadTasks;

    @Before
    public void setup() {
        sut = new MetricsService(metricsDownloadTasks);
    }

    @Test
    public void collectMetrics_allDownloadsSuccessful_shouldReturnAllMetrics() {
        Map allMetrics = MetricsTestResources.allDownloadsOK();
        prepareDownloadMocks(DownloadTasksStatus.ALL_DOWNLOADS_SUCCESS);

        Map actualMetrics = sut.collectMetrics(org);

        assertEquals(allMetrics, actualMetrics);
        verifyMocks();
    }

    @Test
    public void collectMetrics_datasetCountDownloadFailed_shouldReturnMetricsWithoutDataset() {
        Map metricsWithoutDataset = MetricsTestResources.withoutDataset();
        prepareDownloadMocks(DownloadTasksStatus.DATASET_COUNT_DOWNLOAD_FAIL);

        Map actualMetrics = sut.collectMetrics(org);

        assertEquals(metricsWithoutDataset, actualMetrics);
        verifyMocks();
    }

    @Test
    public void collectMetrics_orgQuotaDownloadFailed_shouldReturnMetricsWithoutDataset() {
        Map metricsWithoutQuotaRelated = MetricsTestResources.withoutQuotaRelated();
        prepareDownloadMocks(DownloadTasksStatus.ORG_QUOTA_DOWNLOAD_FAIL);

        Map actualMetrics = sut.collectMetrics(org);

        assertEquals(metricsWithoutQuotaRelated, actualMetrics);
        verifyMocks();
    }

    @Test
    public void collectMetrics_orgSummaryDownloadFailed_shouldReturnMetricsWithoutDataset() {
        Map metricsWithoutMemory = MetricsTestResources.metricsWithoutMemory();
        prepareDownloadMocks(DownloadTasksStatus.ORG_SUMMARY_DOWNLOAD_FAIL);

        Map actualMetrics = sut.collectMetrics(org);

        assertEquals(metricsWithoutMemory, actualMetrics);
        verifyMocks();
    }

    @Test
    public void collectMetrics_orgUserListDownloadFailed_shouldReturnMetricsWithoutUserList() {
        Map metricsWithoutUserList = MetricsTestResources.withoutUserList();
        prepareDownloadMocks(DownloadTasksStatus.ORG_USER_LIST_DOWNLOAD_FAIL);

        Map actualMetrics = sut.collectMetrics(org);

        assertEquals(metricsWithoutUserList, actualMetrics);
        verifyMocks();
    }

    @Test
    public void collectMetrics_spaceGuidsDownloadFailed_shouldReturnMetricsWithoutDataset() {
        Map metricsWithoutSpaceRelated = MetricsTestResources.metricsWithoutSpaceRelated();
        prepareDownloadMocks(DownloadTasksStatus.SPACE_GUIDS_DOWNLOAD_FAIL);

        Map actualMetrics = sut.collectMetrics(org);

        assertEquals(metricsWithoutSpaceRelated, actualMetrics);
        verifyMocks();
    }

    @Test
    public void collectMetrics_space1MetricsDownloadFailed_shouldReturnMetricsWithoutDataset() {
        Map metricsWithoutSpaceRelated = MetricsTestResources.metricsWithoutSpaceRelated();
        prepareDownloadMocks(DownloadTasksStatus.SPACE1_METRICS_DOWNLOAD_FAIL);

        Map actualMetrics = sut.collectMetrics(org);

        assertEquals(metricsWithoutSpaceRelated, actualMetrics);
        verifyMocks(false);
    }

    private void prepareDownloadMocks(DownloadTasksStatus tasksStatus) {
        when(metricsDownloadTasks.getPrivateDatasetCount(org))
            .thenReturn(
                tasksStatus == DownloadTasksStatus.DATASET_COUNT_DOWNLOAD_FAIL
                    ? TestDownloadTasks.failedDownload()
                    : TestDownloadTasks.correctPrivateDatasetCount());

        when(metricsDownloadTasks.getPublicDatasetCount(org))
            .thenReturn(
                tasksStatus == DownloadTasksStatus.DATASET_COUNT_DOWNLOAD_FAIL
                    ? TestDownloadTasks.failedDownload()
                    : TestDownloadTasks.correctPublicDatasetCount());

        when(metricsDownloadTasks.getOrgQuota(org))
            .thenReturn(
                tasksStatus == DownloadTasksStatus.ORG_QUOTA_DOWNLOAD_FAIL
                    ? TestDownloadTasks.failedDownload()
                    : TestDownloadTasks.correctOrgQuota());

        when(metricsDownloadTasks.getOrgSummary(org))
            .thenReturn(
                tasksStatus == DownloadTasksStatus.ORG_SUMMARY_DOWNLOAD_FAIL
                    ? TestDownloadTasks.failedDownload()
                    : TestDownloadTasks.correctOrgSummary());

        when(metricsDownloadTasks.getOrgUserList(org))
                .thenReturn(
                        tasksStatus == DownloadTasksStatus.ORG_USER_LIST_DOWNLOAD_FAIL
                                ? TestDownloadTasks.failedDownload()
                                : TestDownloadTasks.correctOrgUserList());

        when(metricsDownloadTasks.getSpacesGuids(org))
            .thenReturn(
                tasksStatus == DownloadTasksStatus.SPACE_GUIDS_DOWNLOAD_FAIL
                    ? TestDownloadTasks.failedDownload()
                    : TestDownloadTasks.correctSpacesGuids(space1, space2));

        when(metricsDownloadTasks.getSpaceMetricsSingle(space1))
            .thenReturn(
                tasksStatus == DownloadTasksStatus.SPACE1_METRICS_DOWNLOAD_FAIL
                    ? TestDownloadTasks.failedDownload()
                    : TestDownloadTasks.correctSpace1Metrics());

        when(metricsDownloadTasks.getSpaceMetricsSingle(space2))
            .thenReturn(
                tasksStatus == DownloadTasksStatus.SPACE2_METRICS_DOWNLOAD_FAIL
                    ? TestDownloadTasks.failedDownload()
                    : TestDownloadTasks.correctSpace2Metrics());
    }

    public void verifyMocks() {
        verifyMocks(true);
    }

    public void verifyMocks(boolean spaceGuidsDownloadSuccessful) {
        verify(metricsDownloadTasks).getPrivateDatasetCount(org);
        verify(metricsDownloadTasks).getOrgQuota(org);
        verify(metricsDownloadTasks).getOrgSummary(org);
        verify(metricsDownloadTasks).getOrgUserList(org);
        verify(metricsDownloadTasks).getSpacesGuids(org);
        verify(metricsDownloadTasks).getLatestEvents(org);
        if (!spaceGuidsDownloadSuccessful) {
            verify(metricsDownloadTasks).getSpaceMetricsSingle(space1);
            verify(metricsDownloadTasks).getSpaceMetricsSingle(space2);
        }
    }
}
