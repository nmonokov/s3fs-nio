package org.carlspring.cloud.storage.s3fs.path;

import org.carlspring.cloud.storage.s3fs.S3FileSystem;
import org.carlspring.cloud.storage.s3fs.S3Path;
import org.carlspring.cloud.storage.s3fs.S3UnitTestBase;
import org.carlspring.cloud.storage.s3fs.util.S3EndpointConstant;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.carlspring.cloud.storage.s3fs.util.S3EndpointConstant.S3_GLOBAL_URI_TEST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResolveTest
        extends S3UnitTestBase
{


    @BeforeEach
    public void setup()
            throws IOException
    {
        s3fsProvider = getS3fsProvider();
        fileSystem = s3fsProvider.newFileSystem(S3EndpointConstant.S3_GLOBAL_URI_TEST, null);
    }

    @AfterEach
    public void tearDown()
            throws IOException
    {
        s3fsProvider.close((S3FileSystem) fileSystem);
        fileSystem.close();
    }

    private S3Path getPath(String path)
    {
        return s3fsProvider.getFileSystem(S3_GLOBAL_URI_TEST).getPath(path);
    }

    @Test
    void resolve()
    {
        assertEquals(getPath("/bucket/path/to/dir/child/xyz"),
                     getPath("/bucket/path/to/dir/").resolve(getPath("child/xyz")));
        assertEquals(getPath("/bucket/path/to/dir/child/xyz"), getPath("/bucket/path/to/dir/").resolve("child/xyz"));

        assertEquals(getPath("/bucket/path/to/dir/child/xyz"),
                     getPath("/bucket/path/to/dir").resolve(getPath("child/xyz")));
        assertEquals(getPath("/bucket/path/to/dir/child/xyz"), getPath("/bucket/path/to/dir").resolve("child/xyz"));

        assertEquals(getPath("/bucket/path/to/file"), getPath("/bucket/path/to/file").resolve(getPath("")));
        assertEquals(getPath("/bucket/path/to/file"), getPath("/bucket/path/to/file").resolve(""));

        assertEquals(getPath("path/to/file/child/xyz"), getPath("path/to/file").resolve(getPath("child/xyz")));
        assertEquals(getPath("path/to/file/child/xyz"), getPath("path/to/file").resolve("child/xyz"));

        assertEquals(getPath("path/to/file"), getPath("path/to/file").resolve(getPath("")));
        assertEquals(getPath("path/to/file"), getPath("path/to/file").resolve(""));

        assertEquals(getPath("/bucket2/other/child"),
                     getPath("/bucket/path/to/file").resolve(getPath("/bucket2/other/child")));
        assertEquals(getPath("/bucket2/other/child"), getPath("/bucket/path/to/file").resolve("/bucket2/other/child"));
    }

}
