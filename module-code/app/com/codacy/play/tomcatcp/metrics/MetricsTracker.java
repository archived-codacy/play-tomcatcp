/*
 * Copyright (C) 2013,2014 Brett Wooldridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codacy.play.tomcatcp.metrics;

/**
 * This class only supports realtime, not historical metrics.
 * <p>
 * The methods are left empty on purpose to allow a tracker without operations
 *
 * @author Brett Wooldridge
 */
public class MetricsTracker implements AutoCloseable {

    public void recordConnectionAcquiredNanos(final long elapsedAcquiredNanos) {
        //check class description
    }

    public void recordConnectionUsageMillis(final long elapsedBorrowedMillis) {
        //check class description
    }

    public void recordConnectionTimeout() {
        //check class description
    }

    @Override
    public void close() {
        //check class description
    }
}
