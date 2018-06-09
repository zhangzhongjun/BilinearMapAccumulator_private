# 基于双线性对的聚合器

## 参考论文
Supporting Non-membership Proofs with Bilinear-map Accumulators.pdf
## 算法一：生成公私钥
私钥={生成元g，Zr域中的一个随机元素k}
公钥={$$ g^k, g^{k^2}, g^{k^3}, ... $$}
## 算法二：计算一个集合的accumulator
![1](imgs/1.PNG)
其中k是私钥，X是要聚合的集合，x是X中的元素

## 算法三：计算某元素的witness
![2](imgs/2.PNG)

## 算法四：验证元素的witness
需要判断两件事：
![3](imgs/3.PNG)
如果不满足，则说明用户提交了一个非法的请求
![4](imgs/4.PNG)
如果满足，则该witness成立；否则该witness不成立
## 算法五：计算某元素的Non-Witness
同算法三
## 算法六：验证元素的non-witness
将算法四取反
## 算法七：计算一个集合的witness
$$ w_y=g^{\prod_{y \in X-Y} y+s}  $$
## 算法八：验证集合witness
$$ e(w_y,g^ {\prod_{y \in Y} y+s}) = e(g^ {\prod_{x \in X} x+s} , g) $$



如果满足，则witness成立；否则不成立