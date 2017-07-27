package technolifestyle.com.imageslider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lombok.Getter;

public class FlipperView {

    private FlipperView.OnFlipperClickListener onFLipperClickListener;
    @Getter
    private String description;
    @DrawableRes
    @Getter
    private int imageRes;
    @Getter
    private String imageUrl;
    @Getter
    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    private Context context;

    public FlipperView(Context context) {
        this.context = context;
    }

    public FlipperView setDescription(String description) {
        this.description = description;
        return this;
    }

    public FlipperView setImageUrl(String imageUrl) {
        if (imageRes != 0) {
            throw new IllegalStateException("Can't set multiple images");
        }
        this.imageUrl = imageUrl;
        return this;
    }

    public FlipperView setImageDrawable(int imageDrawable) {
        if (!TextUtils.isEmpty(imageUrl)) {
            throw new IllegalStateException("Can't set multiple images");
        }
        this.imageRes = imageDrawable;
        return this;
    }

    public FlipperView setImageScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public View getView() {
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(context).inflate(R.layout.image_flipper_layout_item, null, true);
        ImageView autoSliderImage = (ImageView) v.findViewById(R.id.iv_auto_image_slider);
        TextView description = (TextView) v.findViewById(R.id.tv_auto_image_slider);
        description.setText(getDescription());
        bindData(v, autoSliderImage);
        return v;
    }

    public FlipperView setOnFlipperClickListener(FlipperView.OnFlipperClickListener l) {
        onFLipperClickListener = l;
        return this;
    }

    private void bindData(View v, ImageView autoSliderImage) {
        final FlipperView con = this;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onFLipperClickListener != null) {
                    onFLipperClickListener.onFlipperClick(con);
                }
            }
        });
        try {
            autoSliderImage.setScaleType(getScaleType());
            if (imageUrl != null) {
                Glide.with(context).load(imageUrl)
                        .thumbnail(0.1f)
                        .into(autoSliderImage);

            } else {
                Glide.with(context).load(imageRes)
                        .thumbnail(0.1f)
                        .into(autoSliderImage);
            }
        } catch (Exception exception) {
            Log.d("Exception", exception.getMessage());
        }
    }

    public interface OnFlipperClickListener {
        void onFlipperClick(FlipperView flipperView);
    }
}
