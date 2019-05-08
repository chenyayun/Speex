# Speex

这是一个Android Studio工程，集成了speex库，可以将音频编码成speex格式，也可以将speex格式的音频文件解码播放。

### Speex介绍

* Speex编解码器[http://www.speex.org](http://www.speex.org)的存在是因为需要一款开源且免软件专利使用费的语音编解码器，这是任何开源软件可用的必要条件。本质上来说，Speex相对于语音正如Vorbis(注：免费音乐格式)相对于音频/音乐。不像许多其他语音编解码器，Speex不是为移动电话而设计，而是为分封网络(packet network)和网络电话(VoIP)而设计的。所以当然支持基于文件的压缩。

* Speex设计灵活，支持多种不同的语音质量和比特率。对高质量语音的支持也就意味着Speex不仅能编码窄带语音(电话语音质量，8kHz采样率)，也能编码宽带语音(16kHz采样率）。

* 为VoIP而不是移动电话而设计意味着Speex对丢失的数据包鲁棒，但对损坏的数据包不鲁棒。这基于在VoIP中数据包要么完整到达要么不能到达这一假设的。因为Speex针对于多种设备，所以它复杂性适度(可调节)并且占用较少内存。

* 考虑这些设计目标，我们选用CELP作为编码技术。其中一个主要原因是CELP很早就被证明在低比特率(如4.8kbps的DoD CELP)和高比特率(如16kbps的G.728)都能稳定可靠工作。


### Speex 编码流程
 
```
1、定义一个SpeexBits类型变量bits和一个Speex编码器状态变量enc_state。
2、调用speex_bits_init(&bits)初始化bits。
3、调用speex_encoder_init(&speex_nb_mode)来初始化enc_state。其中speex_nb_mode是SpeexMode类型的变量，表示的是窄带模式。还有speex_wb_mode表示宽带模式、speex_uwb_mode表示超宽带模式。
4、调用函数int speex_encoder_ ctl(void *state, int request, void *ptr)来设定编码器的参数，其中参数state表示编码器的状态；参数request表示要定义的参数类型，如SPEEX_ GET_ FRAME_SIZE表示设置帧大小，SPEEX_ SET_QUALITY表示量化大小，这决定了编码的质量；参数ptr表示要设定的值。
    可通过speex_encoder_ctl(enc_state, SPEEX_GET_FRAME_SIZE, &frame_size) 和speex_encoder_ctl(enc_state, SPEEX_SET_QUALITY, &quality)来设定编码器的参数。
5、初始化完毕后，对每一帧声音作如下处理：调用函数speex_bits_reset(&bits)再次设定SpeexBits，然后调用函数speex_encode(enc_state, input_frame, &bits)，参数bits中保存编码后的数据流。
6、编码结束后，调用函数speex_bits_destroy (&bits)， speex_encoder_destroy (enc_state)来
```

    
### Speex解码流程
   
```
1、 定义一个SpeexBits类型变量bits和一个Speex编码状态变量enc_state。
2、 调用speex_bits_init(&bits)初始化bits。
3、 调用speex_decoder_init (&speex_nb_mode)来初始化enc_state。
4、 调用函数speex_decoder_ctl (void *state, int request, void *ptr)来设定编码器的参数。
5、 调用函数 speex_decode(void *state, SpeexBits *bits, float *out)对参数bits中的音频数据进行解编码，参数out中保存解码后的数据流。
6、 调用函数speex_bits_destroy(&bits), speex_ decoder_ destroy (void *state)来关闭和销毁SpeexBits和解码器。
```

### 工程介绍
JNI封装了如下native方法可供使用：

```
public native int open(int compression);
public native int getFrameSize();
public native int decode(byte encoded[], short lin[], int size);
public native int encode(short lin[], int offset, byte encoded[], int size);
public native void close();
```

SpeexRecorder.java：用于将麦克风音频编码成speex格式并保存；

SpeexPlayer.java：用于播放speex编码格式的音频文件；

SpeexDecoder.java：用于解码speex编码格式的音频文件；

SpeexEncoder.java：将音频编码成speex格式。

### 使用说明


使用的时候拷贝libspeex-debug.aar到模块的lib目录下，并添加依赖即可。

注：如需libspeex的源代码，请联系QQ:835405050
