package org.izce.mongodb_recipe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.izce.mongodb_recipe.services.FilesystemStorageServiceImpl;
import org.izce.mongodb_recipe.services.StorageProperties;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;

import reactor.core.publisher.Flux;

@Disabled
public class FilesystemStorageServiceImplTest {
	private static final String CONSTANT_STRING_VALUE = "Hello, World";
	private StorageProperties properties = new StorageProperties();
	private StorageService service;

	@BeforeEach
	public void init() {
		properties.setLocation("target/files/" + Math.abs(new Random().nextLong()));
		service = new FilesystemStorageServiceImpl(properties);
		service.init();
	}

	@Test
	public void loadNonExistent() throws Exception {
		assertThat(service.load("foo.txt").block()).doesNotExist();
	}

	@Test
	public void saveAndLoad() throws Exception {
		var bodyBuilder = new MultipartBodyBuilder();
		bodyBuilder.part("foo", CONSTANT_STRING_VALUE.getBytes()).header("Content-Disposition", "form-data; name=foo; filename=foo.txt");
		var map = bodyBuilder.build();
		
		FilePart filePart = mock(FilePart.class);
		when(filePart.filename()).thenReturn("foo.txt");
		when(filePart.headers()).thenReturn(map.getFirst("foo").getHeaders());
		DataBuffer dataBuffer = new DefaultDataBufferFactory().allocateBuffer();
		dataBuffer.write(CONSTANT_STRING_VALUE.getBytes());
		when(filePart.content()).thenReturn(Flux.just(dataBuffer));
		
		service.store(filePart);
		//assertThat(service.load("foo.txt").block()).exists();
	}
//
//	@Test
//	public void saveRelativePathNotPermitted() {
//		assertThrows(RuntimeException.class, () -> {
//			service.store(new MockMultipartFile("foo", "../foo.txt",
//					MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()));
//		});
//	}
//
//	@Test
//	public void saveAbsolutePathNotPermitted() {
//		assertThrows(RuntimeException.class, () -> {
//			service.store(new MockMultipartFile("foo", "/etc/passwd",
//					MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()));
//		});
//	}
//
//	@Test
//	@EnabledOnOs({OS.LINUX})
//	public void saveAbsolutePathInFilenamePermitted() {
//		//Unix file systems (e.g. ext4) allows backslash '\' in file names.
//		String fileName="\\etc\\passwd";
//		service.store(new MockMultipartFile(fileName, fileName,
//				MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()));
//		assertTrue(Files.exists(
//				Paths.get(properties.getLocation()).resolve(Paths.get(fileName))));
//	}
//
//	@Test
//	public void savePermitted() {
//		service.store(new MockMultipartFile("foo", "bar/../foo.txt",
//				MediaType.TEXT_PLAIN_VALUE, "Hello, World".getBytes()));
//	}


}
