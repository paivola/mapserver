arg_list = argv();
filename = arg_list{1};
file = dlmread(filename, ",", 'A2..Z999');

x = 1:1:length(file(:,1));

disp(file(:, 1));

for i=1:length(file(1, :))
   plot(x, file(:,i));
   hold on
endfor

xlabel("time");
ylabel("values");

figure(1);
fullname = [filename ".png"];
print(fullname, "-dpng");
