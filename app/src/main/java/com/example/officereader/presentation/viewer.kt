package com.example.officereader.presentation

import android.content.*
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import coil.compose.AsyncImage
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.officereader.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.math.sqrt


@Composable
fun PdfViewer(
    modifier: Modifier = Modifier,
    uri: Uri,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp)
) {
    val redererScope = rememberCoroutineScope()
    val mutex = remember {
        Mutex()
    }
    val rederer by produceState<PdfRenderer?>(null, uri) {
        redererScope.launch(Dispatchers.IO) {
            val input = ParcelFileDescriptor.open(uri.toFile(), ParcelFileDescriptor.MODE_READ_ONLY)
            value = PdfRenderer(input)
        }
        awaitDispose {
            val currentRenderer = value
            redererScope.launch(Dispatchers.IO) {
                mutex.withLock {
                    currentRenderer?.close()
                }
            }
        }
    }
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    val imageLoadingScope = rememberCoroutineScope()
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val width = with(LocalDensity.current) {
            maxWidth.toPx()
        }.toInt()
        val height = (width * sqrt(2f)).toInt()
        val pageCount by remember(rederer) {
            derivedStateOf { rederer?.pageCount ?: 0 }
        }
        LazyColumn(verticalArrangement = verticalArrangement){
            items(count = pageCount, key = {index -> "$uri-$index"}){index ->
                val cacheKey = MemoryCache.Key("$uri-$index")
                var bitmap by remember {
                    mutableStateOf(imageLoader.memoryCache?.get(cacheKey) as? Bitmap?)
                }
                if (bitmap == null){
                    DisposableEffect(uri, index){
                        val job = imageLoadingScope.launch(Dispatchers.IO) {
                            val destinationBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                            mutex.withLock {
                                Log.i("PDF RENDER","Loading PDF $uri - page $index/$pageCount")
                                if (!coroutineContext.isActive) return@launch
                                try {
                                    rederer?.let {
                                        it.openPage(index).use { page ->
                                            page.render(
                                                destinationBitmap,
                                                null,
                                                null,
                                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    //Just catch and return in case the renderer is being closed
                                    return@launch
                                }
                            }
                            bitmap = destinationBitmap
                        }
                        onDispose {
                            job.cancel()
                        }
                    }
                    Box(modifier = Modifier
                        .background(Color.White)
                        .aspectRatio(1f / sqrt(2f))
                        .fillMaxWidth())
                } else {
                    val request = ImageRequest.Builder(context)
                        .size(width, height)
                        .memoryCacheKey(cacheKey)
                        .data(bitmap)
                        .build()

                    AsyncImage(
                        model = request,
                        modifier = Modifier
                            .background(Color.White)
                            .aspectRatio(1f / sqrt(2f))
                            .fillMaxWidth(),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.placeholder),
                        contentDescription = "Page ${index + 1} of $pageCount"
                    )
                }
            }
        }
    }
}

@Composable
fun OpenFile(context: Context, url: File){
    val file: File = url
    val uri:Uri = Uri.fromFile(file)
    Log.d("pathAttach",uri.toString())
    val intent = Intent(Intent.ACTION_VIEW)

    if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
        // Word document
        intent.setDataAndType(uri, "application/msword")
    } else if(url.toString().contains(".pdf")) {
        // PDF file
        intent.setDataAndType(uri, "application/pdf")
    } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
        // Powerpoint file
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
    } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
        // Excel file
        intent.setDataAndType(uri, "application/vnd.ms-excel")
    } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
        // WAV audio file
        intent.setDataAndType(uri, "application/x-wav")
    } else if(url.toString().contains(".rtf")) {
        // RTF file
        intent.setDataAndType(uri, "application/rtf")
    } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
        // WAV audio file
        intent.setDataAndType(uri, "audio/x-wav")
    } else if(url.toString().contains(".gif")) {
        // GIF file
        intent.setDataAndType(uri, "image/gif")
    } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
        // JPG file
        intent.setDataAndType(uri, "image/jpeg")
    } else if(url.toString().contains(".txt")) {
        // Text file
        intent.setDataAndType(uri, "text/plain")
    } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
        // Video files
        intent.setDataAndType(uri, "video/*")
    } else {
        intent.setDataAndType(uri, "*/*")
    }

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    context.startActivity(intent)
}

@Composable
fun getThumbnail(context: Context, contentUri: Uri, width: Int, height: Int, imageId: Long): Bitmap {
    val mContentResolver: ContentResolver = context.contentResolver
    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) {
        return mContentResolver.loadThumbnail(contentUri, Size(width / 2, height / 2), null)
    } else {
        return MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, imageId, MediaStore.Images.Thumbnails.MINI_KIND, null)
    }
}
